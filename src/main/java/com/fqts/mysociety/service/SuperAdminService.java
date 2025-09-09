package com.fqts.mysociety.service;

import com.fqts.mysociety.dto.request.MemberLoginRequestDTO;
import com.fqts.mysociety.dto.response.SuperAdminLoginResponseDTO;

public interface SuperAdminService {

  SuperAdminLoginResponseDTO login(MemberLoginRequestDTO loginRequestDTO);

}
