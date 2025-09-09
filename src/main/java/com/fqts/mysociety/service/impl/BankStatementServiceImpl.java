package com.fqts.mysociety.service.impl;

import com.fqts.mysociety.dto.request.BankColumnMapping;
import com.fqts.mysociety.exception.BankStatementProcessingException;
import com.fqts.mysociety.model.BankStatement;
import com.fqts.mysociety.model.IncomingFile;
import com.fqts.mysociety.repository.BankStatementRepository;
import com.fqts.mysociety.service.BankStatementService;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BankStatementServiceImpl implements BankStatementService {

  private static final Logger logger = LoggerFactory.getLogger(BankStatementServiceImpl.class);

  private static final Map<String, BankColumnMapping> BANK_COLUMN_MAPPINGS = Map.of(
      "SBI_SAVING", new BankColumnMapping(0, -1, 4, 5, 6, 7, 8),
      "SBI_CURRENT", new BankColumnMapping(0, 1, 2, 3, 5, 6, 7),
      "AXIS", new BankColumnMapping(0, -1, 2, 1, 3, 4, 5),
      "BOM", new BankColumnMapping(1, -1, 2, 3, 4, 5, 6)
  );

  private static final Map<String, List<String>> BANK_HEADERS = Map.of(
      "SBI_SAVING", List.of("Date", "Details", "Ref No./Cheque No", "Debit", "Credit", "Balance"),
      "SBI_CURRENT",
      List.of("Txn Date", "Value Date", "Description", "Ref No./Cheque No.", "Branch Code", "Debit",
          "Credit", "Balance"),
      "AXIS",
      List.of("Tran Date", "Chq No", "Particulars", "Debit", "Credit", "Balance", "Init.Br"),
      "BOM",
      List.of("Sr No", "Date", "Particulars", "Cheque/Reference No", "Debit", "Credit", "Balance",
          "Channel")
  );

  private final BankStatementRepository bankStatementRepository;

  @Override
  @Transactional
  public void processAndSaveStatements(IncomingFile incomingFile, MultipartFile file) {
    String bankName = incomingFile.getBankName().toUpperCase();
    logger.info("Processing Excel bank statement for bank: {}", bankName);

    List<String> expectedHeaders = BANK_HEADERS.get(bankName);
    if (expectedHeaders == null) {
      throw new BankStatementProcessingException("Unsupported bank: " + bankName);
    }

    try (InputStream is = file.getInputStream(); XSSFWorkbook workbook = new XSSFWorkbook(is)) {
      XSSFSheet sheet = workbook.getSheetAt(0);
      int headerRowIndex = findHeaderRow(sheet, expectedHeaders);

      if (headerRowIndex == -1) {
        throw new BankStatementProcessingException(
            "Excel header not matched for bank: " + bankName);
      }

      BankColumnMapping mapping = BANK_COLUMN_MAPPINGS.get(bankName);

      for (int i = headerRowIndex + 1; i <= sheet.getLastRowNum(); i++) {
        Row row = sheet.getRow(i);
        if (row == null || row.getCell(mapping.getTransactionDateIndex()) == null) {
          continue;
        }

        String firstCellValue = getCellValue(row.getCell(mapping.getTransactionDateIndex())).trim();
        if (firstCellValue.equalsIgnoreCase("Date")) {
          logger.info("Skipping internal header row at index: {}", i);
          continue;
        }

        if (!isValidDate(firstCellValue)) {
          logger.warn("Skipping row with invalid date '{}' at index {}", firstCellValue, i);
          continue;
        }

        try {
          BankStatement statement = parseRowToBankStatement(row, mapping, incomingFile);
          if (statement == null) {
            logger.debug("Skipped null or duplicate statement at row index: {}", i);
            continue;
          }

          // INSERT (save new entry)
          bankStatementRepository.save(statement);
          logger.info("Saved new transaction from Excel row {}: {}", i + 1,
              statement.getBankStatementId());

        } catch (Exception ex) {
          logger.error("Error processing Excel row {}: {}", i + 1, ex.getMessage());
        }
      }

    } catch (IOException e) {
      throw new BankStatementProcessingException("Could not read Excel file", e);
    }
  }

  private int findHeaderRow(Sheet sheet, List<String> expectedHeaders) {
    List<String> expectedLower = expectedHeaders.stream().map(String::toLowerCase)
        .collect(Collectors.toList());
    for (Row row : sheet) {
      List<String> rowHeaders = new ArrayList<>();
      for (Cell cell : row) {
        rowHeaders.add(getCellValue(cell).toLowerCase());
      }
      if (rowHeaders.containsAll(expectedLower)) {
        logger.info("Detected header at row {}", row.getRowNum());
        return row.getRowNum();
      }
    }
    return -1;
  }

  private boolean isValidDate(String dateStr) {
    if (dateStr == null || dateStr.isBlank()) {
      return false;
    }
    try {
      parseDateWithFallback(dateStr);
      return true;
    } catch (ParseException e) {
      return false;
    }
  }

  private BankStatement parseRowToBankStatement(Row row, BankColumnMapping mapping,
      IncomingFile file) throws ParseException {
    String txnDate = getCellValue(row, mapping.getTransactionDateIndex());
    String valueDate =
        mapping.getValueDateIndex() >= 0 ? getCellValue(row, mapping.getValueDateIndex()) : null;
    String desc = getCellValue(row, mapping.getDescriptionIndex());
    String chqNo = getCellValue(row, mapping.getChequeNumberIndex());
    String debit = getCellValue(row, mapping.getDebitAmountIndex());
    String credit = getCellValue(row, mapping.getCreditAmountIndex());
    String balance = getCellValue(row, mapping.getBalanceIndex());

    if (txnDate.isBlank() && desc.isBlank() && debit.isBlank() && credit.isBlank()) {
      return null;
    }

    Date txnParsed = parseDateWithFallback(txnDate);
    Date valParsed = valueDate != null ? parseDateWithFallback(valueDate) : null;
    BigDecimal debitAmt = parseAmount(debit);
    BigDecimal creditAmt = parseAmount(credit);
    BigDecimal balanceAmt = parseAmount(balance);

    String fingerprint =
        (txnParsed != null ? txnParsed.getTime() : "null") + "|" + chqNo + "|" + debitAmt + "|"
            + creditAmt + "|" + desc;
    String hash = DigestUtils.sha256Hex(fingerprint);

    // UPSERT check
    if (bankStatementRepository.existsByTransactionHash(hash)) {
      logger.warn("Duplicate transaction detected. Skipping row with hash: {}", hash);
      return null;
    }

    BankStatement statement = new BankStatement();
    statement.setBankStatementId(UUID.randomUUID());
    statement.setIncomingFile(file);
    statement.setTransactionDate(txnParsed);
    statement.setValueDate(valParsed);
    statement.setDescription(desc);
    statement.setChequeNumber(chqNo);
    statement.setDebitAmount(debitAmt);
    statement.setCreditAmount(creditAmt);
    statement.setBalance(balanceAmt);
    statement.setProcessedAt(new Date());
    statement.setTransactionHash(hash);
    return statement;
  }

  private String getCellValue(Row row, int index) {
    if (row == null || index < 0 || row.getCell(index) == null) {
      return "";
    }
    return getCellValue(row.getCell(index));
  }

  private String getCellValue(Cell cell) {
    if (cell == null) {
      return "";
    }
    return switch (cell.getCellType()) {
      case STRING -> cell.getStringCellValue().trim();
      case NUMERIC -> DateUtil.isCellDateFormatted(cell)
          ? new SimpleDateFormat("dd-MM-yyyy").format(cell.getDateCellValue())
          : String.valueOf(cell.getNumericCellValue()).trim();
      case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
      case FORMULA -> switch (cell.getCachedFormulaResultType()) {
        case STRING -> cell.getStringCellValue().trim();
        case NUMERIC -> String.valueOf(cell.getNumericCellValue()).trim();
        case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
        default -> "";
      };
      default -> "";
    };
  }

  private Date parseDateWithFallback(String dateStr) throws ParseException {

    String cleanedDateStr = dateStr.trim().replace("\"", "");
    List<String> formats = List.of("dd-MM-yyyy", "dd/MM/yyyy", "dd-MMM-yy", "dd-MMM-yyyy",
        "dd.MM.yyyy", "dd MMM yyyy");

    for (String format : formats) {
      try {
        return new SimpleDateFormat(format, Locale.ENGLISH).parse(cleanedDateStr);
      } catch (ParseException ignored) {
      }
    }
    throw new ParseException("Unparsable Date: \"" + cleanedDateStr + "\"", 0);
  }

  private BigDecimal parseAmount(String amountStr) {
    if (amountStr == null || amountStr.isBlank() || amountStr.equals("-")) {
      return BigDecimal.ZERO;
    }

    try {
      String cleaned = amountStr.replace(",", "").replace("\"", "").trim();
      if (cleaned.isEmpty() || cleaned.equals("+") || cleaned.equals("-")) {
        return BigDecimal.ZERO;
      }
      cleaned = cleaned.replaceAll("(?i)E\\s*\\+?", "E+").replaceAll("(?i)E\\s*-", "E-");
      if (!cleaned.matches(".*\\d.*")) {
        logger.warn("Amount string '{}' contains no digits. Returning BigDecimal.ZERO.", amountStr);
        return BigDecimal.ZERO;
      }
      return new BigDecimal(cleaned);
    } catch (NumberFormatException e) {
      logger.warn("Invalid amount format '{}'. Returning BigDecimal.ZERO. Error: {}", amountStr,
          e.getMessage());
      return BigDecimal.ZERO;
    }
  }
}
