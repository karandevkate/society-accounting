package com.fqts.mysociety.service;

import com.fqts.mysociety.dto.request.JournalRequestDTO;
import com.fqts.mysociety.dto.response.JournalResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface JournalService {

    JournalResponseDTO createJournal(JournalRequestDTO dto);

//    List<JournalResponseDTO> getJournalsByFlatAndDate(String flatNumber, LocalDate startDate, LocalDate endDate);

    List<JournalResponseDTO> getJournalsByFlatAndDate(String flatNumber, UUID societyId, LocalDate startDate, LocalDate endDate) ;

        Page<JournalResponseDTO> getAllJournalsForAdmin(Pageable pageable);  // NEW: Paginated admin journal view

    void autoGenerateAndEmailJournals();

    List<JournalResponseDTO> getAllJournalDetailsForSociety(UUID societyId);

    List<JournalResponseDTO> getAllJournalDetailsForSocietyAndFlat(UUID societyId, String flatNumber);
}


