package com.fqts.mysociety.controller;

import com.fqts.mysociety.dto.request.MemberApprovalRequestDTO;
import com.fqts.mysociety.service.AdminService;
import com.fqts.mysociety.service.MemberMasterService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private static final Logger log = LoggerFactory.getLogger(AdminController.class);

    private final MemberMasterService memberMasterService;
    private final AdminService adminService;

    @PutMapping("/approve-member/{memberId}")
    public ResponseEntity<String> approveMember(
            @PathVariable("memberId") UUID memberId,
            @RequestBody MemberApprovalRequestDTO requestDTO) {

        adminService.approveMember(memberId, requestDTO.getRole());
        return ResponseEntity.ok("Member approved successfully.");
    }

    @PutMapping("/reject-member/{societyMemberId}")
    public ResponseEntity<String> rejectMember(
            @PathVariable("societyMemberId") UUID societyMemberId,
            @RequestBody MemberApprovalRequestDTO requestDTO) {

        adminService.rejectMember(societyMemberId, requestDTO.getRole());
        return ResponseEntity.ok("Member approved successfully.");
    }


}

