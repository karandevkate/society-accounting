package com.fqts.mysociety.controller;

import com.fqts.mysociety.dto.request.MemberLoginRequestDTO;
import com.fqts.mysociety.dto.response.SuperAdminLoginResponseDTO;
import com.fqts.mysociety.service.SuperAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/superadmin")
@RequiredArgsConstructor
public class SuperAdminController {
    private final SuperAdminService superAdminService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberLoginRequestDTO loginRequestDTO) {
        try {
            SuperAdminLoginResponseDTO responseDTO = superAdminService.login(loginRequestDTO);
            return ResponseEntity.ok(responseDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
