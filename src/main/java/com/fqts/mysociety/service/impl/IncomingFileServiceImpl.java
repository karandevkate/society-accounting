package com.fqts.mysociety.service.impl;

import com.fqts.mysociety.exception.FileProcessingException;
import com.fqts.mysociety.helper.ChecksumHelper;
import com.fqts.mysociety.model.IncomingFile;
import com.fqts.mysociety.repository.IncomingFileRepository;
import com.fqts.mysociety.service.BankStatementService;
import com.fqts.mysociety.service.IncomingFileService;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class IncomingFileServiceImpl implements IncomingFileService {

  private static final Logger log = LoggerFactory.getLogger(IncomingFileServiceImpl.class);

  private final IncomingFileRepository incomingFileRepository;
  private final BankStatementService bankStatementService;

  @Override
  public void uploadBankStatementFile(MultipartFile file, String uploadedBy, String bankName) {
    log.info("Uploading bank statement. UploadedBy: {}, BankName: {}, FileName: {}", uploadedBy,
        bankName, file != null ? file.getOriginalFilename() : "null");

    if (file == null || file.isEmpty()) {
      throw new IllegalArgumentException("Uploaded file is empty or null.");
    }

    String originalFileName = file.getOriginalFilename();

    if (originalFileName == null || !originalFileName.toLowerCase().endsWith(".xlsx")) {
      throw new IllegalArgumentException("Only .xlsx Excel files are allowed.");
    }

    long fileSize = file.getSize();
    String contentType = file.getContentType();
    int numberOfRecords = 0;
    String checksum;

    try {
      checksum = ChecksumHelper.calculateMD5Checksum(file);
      incomingFileRepository.findByChecksum(checksum).ifPresent(existing -> {
        throw new IllegalArgumentException("Duplicate file detected with checksum: " + checksum);
      });
    } catch (IOException e) {
      throw new RuntimeException("Error calculating checksum: " + originalFileName, e);
    }

    try (InputStream inputStream = file.getInputStream();
        Workbook workbook = new XSSFWorkbook(inputStream)) {

      Sheet sheet = workbook.getSheetAt(0);

      for (int i = 0; i <= sheet.getLastRowNum(); i++) {
        Row row = sheet.getRow(i);
        if (row == null) {
          continue;
        }

        boolean isHeader = isHeaderRow(row);
        boolean isData = isDataRow(row);

        if (isHeader) {
          log.debug("Skipping header row at index {}", i);
        } else if (isData) {
          numberOfRecords++;
        }
      }

    } catch (IOException e) {
      throw new FileProcessingException("Failed to read Excel file: " + originalFileName, e);
    }

    IncomingFile incomingFile = new IncomingFile();
    incomingFile.setIncomingFileId(UUID.randomUUID());
    incomingFile.setUploadedDate(new Date());
    incomingFile.setUploadedBy(uploadedBy);
    incomingFile.setFileSize(fileSize);
    incomingFile.setBankName(bankName);
    incomingFile.setOriginalFileName(originalFileName);
    incomingFile.setContentType(contentType);
    incomingFile.setNumberOfRecords(numberOfRecords);
    incomingFile.setChecksum(checksum);

    incomingFileRepository.save(incomingFile);

    bankStatementService.processAndSaveStatements(incomingFile, file);
  }

  private boolean isHeaderRow(Row row) {
    String cell0 = getCellValue(row, 0).toLowerCase();
    String cell1 = getCellValue(row, 1).toLowerCase();
    String cell2 = getCellValue(row, 2).toLowerCase();

    return cell0.contains("date") && cell1.contains("details") && cell2.contains("ref");
  }

  private boolean isDataRow(Row row) {
    String date = getCellValue(row, 0);
    return date.matches("\\d{1,2} \\w{3} \\d{4}");
  }

  private String getCellValue(Row row, int cellIndex) {
    if (row == null) {
      return "";
    }

    Cell cell = row.getCell(cellIndex);
    if (cell == null) {
      return "";
    }

    return switch (cell.getCellType()) {
      case STRING -> cell.getStringCellValue().trim();
      case NUMERIC -> String.valueOf(cell.getNumericCellValue());
      case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
      case FORMULA -> {
        try {
          yield cell.getStringCellValue().trim();
        } catch (IllegalStateException e) {
          yield String.valueOf(cell.getNumericCellValue());
        }
      }
      default -> "";
    };
  }
}
