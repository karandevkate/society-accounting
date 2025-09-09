package com.fqts.mysociety.controller;

import com.fqts.mysociety.dto.request.FlatRegistrationRequest;
import com.fqts.mysociety.dto.request.FlatRequestDTO;
import com.fqts.mysociety.dto.response.FlatResponseDTO;
import com.fqts.mysociety.exception.FlatNotFoundException;
import com.fqts.mysociety.exception.SocietyNotFoundException;
import com.fqts.mysociety.service.FlatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/flats")
@Slf4j
public class FlatController {

    private final FlatService flatService;

    public FlatController(FlatService flatService) {
        this.flatService = flatService;
    }

    @PostMapping
    public FlatResponseDTO createFlat(@RequestBody FlatRequestDTO dto)
            throws SocietyNotFoundException {
        log.info("Received request to create flat: {}", dto);
        return flatService.createFlat(dto);
    }

    @GetMapping("/{societyId}/{flatNumber}")
    public FlatResponseDTO getFlatById(@PathVariable UUID societyId,
                                       @PathVariable String flatNumber) throws FlatNotFoundException {
        log.info("Received request to fetch flat with number: {} in society: {}", flatNumber, societyId);
        return flatService.getFlatById(flatNumber, societyId);
    }


    @GetMapping("/society/{societyId}")
    public List<FlatResponseDTO> getFlatsBySociety(@PathVariable UUID societyId)
            throws SocietyNotFoundException {
        log.info("Received request to fetch flats for societyId: {}", societyId);
        return flatService.getFlatsBySocietyId(societyId);
    }

    @GetMapping("/by-society-member/{societyMemberId}/society/{societyId}")
    public ResponseEntity<List<FlatResponseDTO>> getFlatsBySocietyMemberAndSociety(
            @PathVariable UUID societyMemberId,
            @PathVariable UUID societyId
    ) {
        List<FlatResponseDTO> flats = flatService.getFlatsBySocietyMemberAndSociety(societyMemberId, societyId);

        if (flats.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(flats);
    }

    @PostMapping("/api/society/member/register/flat")
    public ResponseEntity<String> registerMemberToFlat(@RequestBody FlatRegistrationRequest request) {
        flatService.registerMemberToFlat(request.getFlatNumber(), request.getSocietyId(),request.getSocietyMemberId());
        return ResponseEntity.ok("Flat Registered Successfully");
    }

    @PostMapping("/bulk")
    public ResponseEntity<String> uploadFlats(@RequestParam("file") MultipartFile file, @RequestParam("societyId") UUID societyId) {
        try {
            flatService.saveFlatsFromExcel(file, societyId);
            return ResponseEntity.ok("Flats uploaded successfully");
        } catch (SocietyNotFoundException e) {
            return ResponseEntity.status(404).body("Society not found: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to upload flats: " + e.getMessage());
        }
    }

}
