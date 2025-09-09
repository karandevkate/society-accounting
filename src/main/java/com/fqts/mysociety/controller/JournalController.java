package com.fqts.mysociety.controller;

import com.fqts.mysociety.dto.request.JournalRequestDTO;
import com.fqts.mysociety.dto.response.JournalResponseDTO;
import com.fqts.mysociety.service.JournalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/journal")
@RequiredArgsConstructor
public class JournalController {

    private final JournalService journalService;

    @PostMapping
    public ResponseEntity<JournalResponseDTO> createJournal(@RequestBody JournalRequestDTO dto) {
        return ResponseEntity.ok(journalService.createJournal(dto));
    }

    @GetMapping("/society-wise-journal/{societyId}")
    public ResponseEntity<List<JournalResponseDTO>> getAllJournals(@PathVariable("societyId") UUID societyId) {
        return ResponseEntity.ok(journalService.getAllJournalDetailsForSociety(societyId));
    }


    @GetMapping("/flat-wise-journal")
    public ResponseEntity<List<JournalResponseDTO>> getAllJournals(@RequestParam UUID societyId,
                                                                   @RequestParam String flatNumber) {
        return ResponseEntity.ok(journalService.getAllJournalDetailsForSocietyAndFlat(societyId, flatNumber));
    }

    @GetMapping("/filter-by-date")
    public ResponseEntity<List<JournalResponseDTO>> getJournalsByFlatAndDate(
            @RequestParam String flatNumber,
            @RequestParam UUID societyId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return ResponseEntity.ok(
                journalService.getJournalsByFlatAndDate(flatNumber, societyId, startDate, endDate)
        );
    }


    @GetMapping("/admin")
    public ResponseEntity<Page<JournalResponseDTO>> getAllJournalsForAdmin(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "transactionDate"));
        return ResponseEntity.ok(journalService.getAllJournalsForAdmin(pageable));
    }


    @GetMapping()
    public ResponseEntity<List<JournalResponseDTO>> getJournals(
            @RequestParam UUID societyId,
            @RequestParam(required = false) String flatNumber,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<JournalResponseDTO> journals;

        if (flatNumber != null && startDate != null && endDate != null) {
            journals = journalService.getJournalsByFlatAndDate(flatNumber, societyId, startDate, endDate);
        } else if (flatNumber != null) {
            journals = journalService.getAllJournalDetailsForSocietyAndFlat(societyId, flatNumber);
        } else {
            journals = journalService.getAllJournalDetailsForSociety(societyId);
        }

        return ResponseEntity.ok(journals);
    }
}

