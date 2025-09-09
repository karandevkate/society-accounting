package com.fqts.mysociety.service.impl;

import com.fqts.mysociety.dto.request.JournalRequestDTO;
import com.fqts.mysociety.dto.response.JournalResponseDTO;
import com.fqts.mysociety.exception.JournalNotFoundException;
import com.fqts.mysociety.exception.ResourceNotFoundException;
import com.fqts.mysociety.model.*;
import com.fqts.mysociety.repository.*;
import com.fqts.mysociety.service.EmailService;
import com.fqts.mysociety.service.JournalService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class JournalServiceImpl implements JournalService {

    private static final Logger logger = LoggerFactory.getLogger(JournalServiceImpl.class);

    private final JournalRepository journalRepository;
    private final BillCollectionRepository billCollectionRepository;
    private final MatrixChargesRepository matrixChargesRepository;
    private final FlatRepository flatsRepository;
    private final EmailService emailService;
    private final SocietyMemberRepository societyMemberRepository;

    @Override
    public JournalResponseDTO createJournal(JournalRequestDTO dto) {
        logger.info("Creating manual journal for flat: {}, matrixChargesId: {}",
                dto.getFlatNumber(), dto.getMatrixChargesId());

        // Validate inputs
        FlatId flatId = new FlatId(dto.getFlatNumber(), dto.getSocietyId());

        Flat flat = flatsRepository.findById(flatId)
                .orElseThrow(() -> new JournalNotFoundException("Flat not found: " + dto.getFlatNumber()));
        MatrixCharges charges = matrixChargesRepository.findById(dto.getMatrixChargesId())
                .orElseThrow(() -> new JournalNotFoundException("Matrix charges not found: " + dto.getMatrixChargesId()));

        // BillCollection is optional for manual journals (e.g., fines, expenses)
        BillCollection collection = null;
        if (dto.getCollectionId() != null) {
            collection = billCollectionRepository.findById(dto.getCollectionId())
                    .orElseThrow(() -> new JournalNotFoundException("Collection not found: " + dto.getCollectionId()));
        }

        Journal journal = new Journal();
        journal.setBillCollection(collection);
        journal.setMatrixCharges(charges);
        journal.setFlat(flat);
        journal.setDebitAmount(dto.getDebitAmount());
        journal.setCreditAmount(dto.getCreditAmount());
        journal.setTransactionDate(dto.getTransactionDate() != null ? dto.getTransactionDate() : new Date());
        journal.setValueDate(dto.getValueDate() != null ? dto.getValueDate() : new Date());
        journal.setNarration(dto.getNarration() != null ? dto.getNarration() : "Manual journal entry");
        journal.setComments(dto.getComments());

        Journal saved = journalRepository.save(journal);
        logger.info("Journal created with ID: {}", saved.getJournalId());

        // Send email notification if applicable
        String email = societyMemberRepository.findEmailByFlatAndSociety(
                flat.getId().getFlatNumber(), flat.getId().getSocietyId());
        if (email != null && !email.isEmpty()) {
            emailService.sendJournalNotification(email, saved);
        }

        return convertToResponseDTO(saved);
    }

//    @Override
//    public List<JournalResponseDTO> getJournalsByFlatAndDate(String flatNumber, LocalDate startDate, LocalDate endDate) {
//        logger.info("Fetching journals for flat: {} between {} and {}", flatNumber, startDate, endDate);
//
//        List<Journal> journals = journalRepository.findByFlatNumberAndTransactionDateBetween(
//                flatNumber, startDate, endDate);
//
//        return journals.stream()
//                .map(this::convertToResponseDTO)
//                .toList();
//    }


    @Override
    public List<JournalResponseDTO> getJournalsByFlatAndDate(
            String flatNumber, UUID societyId, LocalDate startDate, LocalDate endDate) {

        logger.info("Fetching journals for flat: {} in society: {} between {} and {}",
                flatNumber, societyId, startDate, endDate);

        List<Journal> journals = journalRepository
                .findByFlatNumberAndSocietyIdAndTransactionDateBetween(flatNumber, societyId, startDate, endDate);

        return journals.stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    @Scheduled(cron = "0 0 0 1 * ?") // Runs at midnight on the 1st of every month
    public void autoGenerateAndEmailJournals() {
        logger.info("Starting auto-generation of journals for all flats");

        List<Flat> flats = flatsRepository.findAll();

        for (Flat flat : flats) {
            // Fetch latest matrix charges
            List<MatrixCharges> chargesList = matrixChargesRepository.
                    findBySociety_SocietyIdAndFlat_Id_FlatNumber(flat.getSociety().getSocietyId(),
                            flat.getId().getFlatNumber());

            for (MatrixCharges charges : chargesList) {
                Journal journal = new Journal();
                journal.setFlat(flat);
                journal.setMatrixCharges(charges);
                journal.setDebitAmount(charges.getAmount());
                journal.setCreditAmount(0.0);
                journal.setTransactionDate(new Date());
                journal.setValueDate(new Date());
                journal.setNarration("Monthly maintenance charges for flat " + flat.getId().getFlatNumber());
                journal.setComments("Auto-generated monthly charge: " + charges.getChargesType());

                Journal savedJournal = journalRepository.save(journal);
                logger.info("Auto-generated journal for flat: {}, journalId: {}",
                        flat.getId().getFlatNumber(), savedJournal.getJournalId());

                // Send email notification
                String email = societyMemberRepository.findEmailByFlatAndSociety(
                        flat.getId().getFlatNumber(), flat.getSociety().getSocietyId());
                if (email != null && !email.isEmpty()) {
                    emailService.sendJournalNotification(email, savedJournal);
                }
            }
        }
        logger.info("Completed auto-generation of journals");
    }

    @Override
    public Page<JournalResponseDTO> getAllJournalsForAdmin(Pageable pageable) {
        logger.info("Fetching all journals for admin with page: {}, size: {}",
                pageable.getPageNumber(), pageable.getPageSize());

        return journalRepository.findAll(pageable)
                .map(this::convertToResponseDTO);
    }

    private JournalResponseDTO convertToResponseDTO(Journal journal) {
        JournalResponseDTO dto = new JournalResponseDTO();
        dto.setJournalId(journal.getJournalId());
        dto.setDebitAmount(journal.getDebitAmount());
        dto.setCreditAmount(journal.getCreditAmount());
        dto.setTransactionDate(journal.getTransactionDate());
        dto.setValueDate(journal.getValueDate());
        dto.setNarration(journal.getNarration());
        dto.setComments(journal.getComments());
        dto.setSocietyId(journal.getSociety().getSocietyId());
        if (journal.getBillCollection() != null) {
            dto.setCollectionId(journal.getBillCollection().getCollectionId());
        }

        if (journal.getMatrixCharges() != null) {
            dto.setMatrixChargesId(journal.getMatrixCharges().getMatrixChargesId());
        }

        if (journal.getFlat() != null) {
            dto.setFlatNumber(journal.getFlat().getId().getFlatNumber());
        }

        return dto;
    }

    @Override
    public List<JournalResponseDTO> getAllJournalDetailsForSociety(UUID societyId) {
        logger.info("Fetching all journal details for society with ID: {}", societyId);

        try {
            List<Journal> journals = journalRepository.findBySocietyId(societyId);

            if (journals.isEmpty()) {
                logger.warn("No journal records found for society ID: {} ", societyId);
                throw new ResourceNotFoundException("No journal records found for the given society ID.");
            }

            List<JournalResponseDTO> responseList = journals.stream().map(journal -> {
                JournalResponseDTO dto = new JournalResponseDTO();
                dto.setJournalId(journal.getJournalId());
                dto.setCreditAmount(journal.getCreditAmount());
                dto.setDebitAmount(journal.getDebitAmount());
                dto.setValueDate(journal.getValueDate());
                dto.setTransactionDate(journal.getTransactionDate());
                dto.setNarration(journal.getNarration());
                dto.setComments(journal.getComments());
                dto.setCollectionId(journal.getBillCollection().getCollectionId());
                dto.setMatrixChargesId(journal.getMatrixCharges().getMatrixChargesId());
                dto.setFlatNumber(journal.getFlat().getId().getFlatNumber());
                dto.setSocietyId(societyId);
                return dto;
            }).collect(Collectors.toList());

            logger.info("Successfully fetched {} journal records", responseList.size());
            return responseList;

        } catch (Exception e) {
            logger.error("Error while fetching journal records for society ID: {}", societyId, e);
            throw new RuntimeException("Failed to retrieve journal records. Please try again later.");
        }
    }

    @Override
    public List<JournalResponseDTO> getAllJournalDetailsForSocietyAndFlat(UUID societyId, String flatNumber) {
        logger.info("Fetching all journal details for society with ID: {}", societyId);

        List<Journal> journals = journalRepository.findBySocietyIdAndFlatNumber(societyId, flatNumber);

        if (journals.isEmpty()) {
            logger.warn("No journal records found for society ID: {} & FlatNumber: {}", societyId, flatNumber);
            throw new ResourceNotFoundException("No journal records found for a flatNumber "+ flatNumber + ".");
        }

        return journals.stream().map(journal -> {
            JournalResponseDTO dto = new JournalResponseDTO();
            dto.setJournalId(journal.getJournalId());
            dto.setCreditAmount(journal.getCreditAmount());
            dto.setDebitAmount(journal.getDebitAmount());
            dto.setValueDate(journal.getValueDate());
            dto.setTransactionDate(journal.getTransactionDate());
            dto.setNarration(journal.getNarration());
            dto.setComments(journal.getComments());
            dto.setCollectionId(journal.getBillCollection().getCollectionId());
            dto.setMatrixChargesId(journal.getMatrixCharges().getMatrixChargesId());
            dto.setFlatNumber(flatNumber);
            dto.setSocietyId(societyId);
            return dto;
        }).collect(Collectors.toList());
    }


}