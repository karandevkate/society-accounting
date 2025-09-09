package com.fqts.mysociety.service.impl;

import com.fqts.mysociety.dto.request.MemberLoginRequestDTO;
import com.fqts.mysociety.dto.response.SuperAdminLoginResponseDTO;
import com.fqts.mysociety.exception.InvalidCredentialsException;
import com.fqts.mysociety.exception.MemberNotFoundException;
import com.fqts.mysociety.model.SuperAdmin;
import com.fqts.mysociety.repository.SuperAdminRepository;
import com.fqts.mysociety.service.MemberService;
import com.fqts.mysociety.service.SuperAdminService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SuperAdminServiceImpl implements SuperAdminService {
    private static final Logger log = LoggerFactory.getLogger(MemberService.class);
    private final SuperAdminRepository superAdminRepository;

  public SuperAdminLoginResponseDTO login(MemberLoginRequestDTO loginRequestDTO) {
    log.info("Attempting member login with mobile: {}", loginRequestDTO.getMobile());
    SuperAdmin superAdmin = superAdminRepository.findByMobile(loginRequestDTO.getMobile())
        .orElseThrow(() -> new MemberNotFoundException(
            "Member not found associated with the mobile " + loginRequestDTO.getMobile()));

    if (!superAdmin.getPassword().equals(loginRequestDTO.getPassword())) {
      throw new InvalidCredentialsException("Invalid credentials");
    }

    return new SuperAdminLoginResponseDTO(
        superAdmin.getSuperAdminId(),
        superAdmin.getFullName(),
        superAdmin.getMobile(),
        superAdmin.getEmail(),
        superAdmin.getRole()
    );
  }
}
