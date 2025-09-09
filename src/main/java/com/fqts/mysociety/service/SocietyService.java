package com.fqts.mysociety.service;

import com.fqts.mysociety.dto.request.SocietyRegistrationDto;
import com.fqts.mysociety.dto.request.SocietyRequest;
import com.fqts.mysociety.dto.response.SocietyDetailsWithCountsResponse;
import com.fqts.mysociety.dto.response.SocietyResponse;
import com.fqts.mysociety.dto.response.SocietyWithAdminResponseDto;
import com.fqts.mysociety.exception.InvalidSocietyRequestException;
import com.fqts.mysociety.exception.SocietyNotFoundException;
import com.fqts.mysociety.model.Society;
import com.fqts.mysociety.model.Status;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface SocietyService {

  String approveSociety(UUID id) ;

  String rejectSociety(UUID id) ;

  SocietyResponse getSocietyById(UUID societyId) ;

  Society updateSociety(UUID societyId, SocietyRequest request);

  UUID registerSocietyWithAdmin(SocietyRegistrationDto societyRegistrationDto)
      throws InvalidSocietyRequestException;

  List<SocietyResponse> getAllSocieties();

  List<Society> findByStatusAndRegisteredBefore(Status status, Date registeredBefore);
  
  Society save(Society society);


  SocietyWithAdminResponseDto getSocietyWithAdmins(UUID societyId);

  SocietyDetailsWithCountsResponse getSocietyWithCounts(UUID societyId);
}
