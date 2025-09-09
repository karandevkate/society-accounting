package com.fqts.mysociety.controller;

import com.fqts.mysociety.dto.request.UpdateLastVisitedRequestDTO;
import com.fqts.mysociety.dto.response.SocietyMemberListDTO;
import com.fqts.mysociety.model.FlatId;
import com.fqts.mysociety.service.MemberMasterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberMasterController {

    private final MemberMasterService memberMasterService;

    @GetMapping("/by-society")
    public List<SocietyMemberListDTO> getMembersBySocietyId(@RequestParam UUID societyId) {
        return memberMasterService.getMembersBySocietyId(societyId);
    }

    @GetMapping("/approved/{societyId}")
    public List<SocietyMemberListDTO> getApprovedOrAutoApprovedMembers(@PathVariable UUID societyId) {
        return memberMasterService.getApprovedOrAutoApprovedMembers(societyId);
    }

    @GetMapping("/pending/{societyId}")
    public List<SocietyMemberListDTO> getPendingMembers(@PathVariable UUID societyId) {
        return memberMasterService.getPendingMembers(societyId);
    }

    @PostMapping("/update-last-visited")
    public ResponseEntity<String> updateLastVisited(@RequestBody UpdateLastVisitedRequestDTO request) {
        FlatId flatId = null;

        if (request.getFlatNumber() != null && request.getFlatSocietyId() != null) {
            flatId = new FlatId(request.getFlatNumber(), request.getFlatSocietyId());
        }

        memberMasterService.updateLastVisited(request.getMemberId(), request.getSocietyId(), flatId);
        return ResponseEntity.ok("Last visited society and flat updated successfully.");
    }


}
