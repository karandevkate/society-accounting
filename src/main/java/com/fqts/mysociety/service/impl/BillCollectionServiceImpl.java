package com.fqts.mysociety.service.impl;

import com.fqts.mysociety.dto.request.BillCollectionRequestDTO;
import com.fqts.mysociety.dto.response.BillCollectionResponseDTO;
import com.fqts.mysociety.exception.*;
import com.fqts.mysociety.model.*;
import com.fqts.mysociety.repository.*;
import com.fqts.mysociety.service.BillCollectionService;
import com.fqts.mysociety.service.JournalService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class BillCollectionServiceImpl implements BillCollectionService {

    private static final Logger logger = LoggerFactory.getLogger(BillCollectionServiceImpl.class);
    private static final String UPLOAD_DIR = "uploads/";
    private static final String SYSTEM_AUTO_VERIFIER = "SYSTEM_AUTO";

    private final BillCollectionRepository billCollectionRepository;
    private final FlatRepository flatRepository;
    private final MatrixChargesRepository matrixChargesRepository;
    private final BankStatementRepository bankStatementRepository;
    private final JournalRepository journalRepository; // Added for Journal creation
    private final JournalService journalService; // Added for Journal creation
    private final SocietyRepository societyRepository;

    @Override
    public BillCollectionResponseDTO saveBill(BillCollectionRequestDTO dto, MultipartFile image) {
        logger.info("Saving bill for flat: {}, amount: {}", dto.getFlatNumber(), dto.getPaidAmount());

        // Validate referenced entities
        FlatId flatId = new FlatId(dto.getFlatNumber(), dto.getSocietyId());
        Flat flats = flatRepository.findById(flatId)
                .orElseThrow(() -> new FlatNotFoundException("Flat not found for flat number: "
                        + dto.getFlatNumber() + " and society ID: "
                        + dto.getSocietyId()));

        MatrixCharges matrixCharges = matrixChargesRepository.findById(dto.getMatrixChargesId())
                .orElseThrow(() -> new ResourceNotFoundException("Matrix charge does not exist."));
//        BankStatement bankStatement = bankStatementRepository.findById(dto.getStatementId())
//                .orElseThrow(() -> new ResourceNotFoundException("Bank statement does not exist."));

        Society society = societyRepository.findById(dto.getSocietyId()).orElseThrow(() -> new SocietyNotFoundException("Society does not exist."));

        BillCollection bill = new BillCollection();
        bill.setMatrixCharges(matrixCharges);
        bill.setFlat(flats);
        bill.setPaidAmount(dto.getPaidAmount());
        bill.setPaidDate(dto.getPaidDate());
        bill.setPaymentMode(dto.getPaymentMode());
        bill.setSociety(society);
        bill.setTransactionRefNumber(dto.getTransactionRefNumber());

        if (image != null && !image.isEmpty()) {
            try {
                bill.setImage(image.getBytes());
            } catch (IOException e) {
                throw new ImageProcessingException("Failed to read image data", e);
            }
        }
        bill = billCollectionRepository.save(bill);
        logger.info("Bill saved with collection ID: {}", bill.getCollectionId());
        return convertToResponseDTO(bill);
    }




    @Override
    public BillCollectionResponseDTO manuallyVerifyBill(UUID billId, String verifiedBy) {
        logger.info("Manually verifying bill with ID: {} by user: {}", billId, verifiedBy);
        BillCollection bill = billCollectionRepository.findById(billId)
                .orElseThrow(() -> new BillNotFoundException("Bill with ID " + billId + " not found"));

        if (bill.isVerified()) {
            logger.info("Bill with ID {} is already verified", billId);
            // Return existing bill details without modifying
            return convertToResponseDTO(bill);
        }

        // Check if transactionRefNumber matches any BankStatement
        boolean isVerified = false;
        if (bill.getTransactionRefNumber() != null && !bill.getTransactionRefNumber().isEmpty()) {
            BankStatement matchingStatement = bankStatementRepository
//                    .findByDescriptionContaining(bill.getTransactionRefNumber())
                    .findByDescriptionContainingAndCreditAmount(bill.getTransactionRefNumber(), bill.getPaidAmount())
                    .orElse(null);

            if (matchingStatement != null && matchingStatement.getCreditAmount() != null
                    && matchingStatement.getCreditAmount().doubleValue() == bill.getPaidAmount()) {
                isVerified = true;
                bill.setVerified(true);
                bill.setVerificationDate(new Date());
                bill.setVerifiedBy(verifiedBy);

                // Save updated bill
                bill = billCollectionRepository.save(bill);
                logger.info("Bill with ID {} manually verified by {}", billId, verifiedBy);

                // Create Journal entry
                createJournalForVerifiedBill(bill);
            } else {
                throw new ResourceNotFoundException(
                        "No matching bank statement found for transaction reference: " + bill.getTransactionRefNumber());
            }
        } else {
            throw new IllegalArgumentException("Bill has no transaction reference number for verification");
        }

        return convertToResponseDTO(bill);
    }

    private void createJournalForVerifiedBill(BillCollection bill) {
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

    private BillCollectionResponseDTO convertToResponseDTO(BillCollection bill) {
        BillCollectionResponseDTO response = new BillCollectionResponseDTO();
        response.setCollectionId(bill.getCollectionId());
        response.setMatrixChargesId(bill.getMatrixCharges().getMatrixChargesId());
        response.setFlatNumber(bill.getFlat().getId().getFlatNumber());
        response.setPaidAmount(bill.getPaidAmount());
        response.setPaidDate(bill.getPaidDate());
        response.setPaymentMode(bill.getPaymentMode());
        response.setTransactionRefNumber(bill.getTransactionRefNumber());
        response.setVerified(bill.isVerified());
        response.setSocietyId(bill.getSociety().getSocietyId());
        response.setVerificationDate(bill.getVerificationDate());
        response.setVerifiedBy(bill.getVerifiedBy());
//        response.setStatementId(bill.getBankStatement().getBankStatementId());
        response.setImageUrl(bill.getImage());
        return response;
    }

    @Override
    public BillCollectionResponseDTO getBillById(UUID id) {
        logger.info("Fetching bill with ID: {}", id);
        BillCollection bill = billCollectionRepository.findById(id)
                .orElseThrow(() -> new BillNotFoundException("Bill with ID " + id + " not found"));
        logger.debug("Found bill for flat: {}", bill.getFlat().getId().getFlatNumber());

        return convertToResponseDTO(bill);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BillCollectionResponseDTO> getAllBillBySocietyId(UUID societyId) {
        logger.info("Fetching all bills of Society {}" ,societyId);
        List<BillCollection> bills = billCollectionRepository.findBySocietySocietyId(societyId);
        logger.debug("Found {} bills", bills.size());

        return bills.stream()
                .map(this::convertToResponseDTO)
                .toList();
    }


    @Override
    @Transactional(readOnly = true)
    public List<BillCollectionResponseDTO> getBillsByFlatNumber(String flatNumber) {
        logger.info("Fetching bills for flat number: {}", flatNumber);
        List<BillCollection> bills = billCollectionRepository.findByFlat_Id_FlatNumber(flatNumber);
        logger.debug("Found {} bills for flat {}", bills.size(), flatNumber);

        return bills.stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    @Override
    public void deleteBillById(UUID id) {
        logger.warn("Deleting bill with ID: {}", id);
        billCollectionRepository.deleteById(id);
        logger.info("Bill with ID {} deleted", id);
    }
}