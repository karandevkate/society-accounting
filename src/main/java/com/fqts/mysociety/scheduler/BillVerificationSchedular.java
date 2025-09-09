package com.fqts.mysociety.scheduler;

import com.fqts.mysociety.dto.response.BillCollectionResponseDTO;
import com.fqts.mysociety.model.BankStatement;
import com.fqts.mysociety.model.BillCollection;
import com.fqts.mysociety.model.Journal;
import com.fqts.mysociety.repository.*;
import com.fqts.mysociety.service.JournalService;
import com.fqts.mysociety.service.impl.BillCollectionServiceImpl;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class BillVerificationSchedular {
    private static final Logger logger = LoggerFactory.getLogger(BillCollectionServiceImpl.class);
    private static final String UPLOAD_DIR = "Uploads/";
    private static final String SYSTEM_AUTO_VERIFIER = "SYSTEM_AUTO";

    private final BillCollectionRepository billCollectionRepository;
    private final FlatRepository flatRepository;
    private final MatrixChargesRepository matrixChargesRepository;
    private final BankStatementRepository bankStatementRepository;
    private final JournalRepository journalRepository;
    private final JournalService journalService;
    private final SocietyRepository societyRepository;

    @Scheduled(initialDelay = 1000, fixedDelay = Long.MAX_VALUE)
    @Transactional
    public void autoVerifyBillsFromBankStatements() {
        logger.info("Starting automatic bill verification...");

        List<BillCollectionResponseDTO> unverifiedBills = billCollectionRepository.findByIsVerifiedFalse();

        for (BillCollectionResponseDTO billDTO : unverifiedBills) {
            try {
                if (billDTO.getTransactionRefNumber() != null && !billDTO.getTransactionRefNumber().isEmpty()) {
                    Optional<BankStatement> matchingStatementOpt = bankStatementRepository
                            .findByDescriptionContainingAndCreditAmount(billDTO.getTransactionRefNumber(), billDTO.getPaidAmount());

                    if (matchingStatementOpt.isPresent()) {
                        // Fetch the full BillCollection entity for updating
                        Optional<BillCollection> billOpt = billCollectionRepository.findById(billDTO.getCollectionId());
                        if (billOpt.isPresent()) {
                            BillCollection bill = billOpt.get();
                            bill.setVerified(true);
                            bill.setVerificationDate(new Date());
                            bill.setVerifiedBy("AUTO-VERIFIED BY SYSTEM");
                            billCollectionRepository.save(bill);

                            createJournalForVerifiedBill(bill);
                            logger.info("Bill ID {} auto-verified successfully.", bill.getCollectionId());
                        } else {
                            logger.warn("Bill ID {} not found in database.", billDTO.getCollectionId());
                        }
                    } else {
                        logger.warn("No matching bank statement found for Bill ID {} with Ref {}", billDTO.getCollectionId(), billDTO.getTransactionRefNumber());
                    }
                } else {
                    logger.warn("Bill ID {} skipped due to missing transaction reference.", billDTO.getCollectionId());
                }
            } catch (Exception ex) {
                logger.error("Error while verifying Bill ID {}: {}", billDTO.getCollectionId(), ex.getMessage());
            }
        }
    }

    private void createJournalForVerifiedBill(BillCollection bill) {
//        Society society = societyRepository.findById(bill.getSociety().getSocietyId()).orElseThrow(() -> new SocietyNotFoundException("Society with societyId: " + bill.getSociety().getSocietyId() + " not found"));
        Journal journal = new Journal();
        journal.setBillCollection(bill);
        journal.setMatrixCharges(bill.getMatrixCharges());
        journal.setFlat(bill.getFlat());
        journal.setCreditAmount(bill.getPaidAmount());
        journal.setTransactionDate(bill.getPaidDate());
        journal.setValueDate(bill.getPaidDate());
        journal.setSociety(bill.getSociety());
        journal.setNarration("Payment received for bill collection ID: " + bill.getCollectionId());
        journal.setComments("Auto-generated from bill verification");

        journalRepository.save(journal);
        logger.info("Journal entry created for bill collection ID: {}", bill.getCollectionId());
    }
}