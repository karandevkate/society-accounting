package com.fqts.mysociety.service;

import com.fqts.mysociety.dto.request.FlatRequestDTO;
import com.fqts.mysociety.dto.response.FlatResponseDTO;
import com.fqts.mysociety.exception.FlatNotFoundException;
import com.fqts.mysociety.exception.SocietyNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface FlatService {

    FlatResponseDTO createFlat(FlatRequestDTO dto) throws SocietyNotFoundException;

    FlatResponseDTO getFlatById(String flatNumber, UUID societyId) throws FlatNotFoundException ;
    List<FlatResponseDTO> getFlatsBySocietyId(UUID societyId) throws SocietyNotFoundException;

    List<FlatResponseDTO> getFlatsBySocietyMemberAndSociety(UUID societyMemberId, UUID societyId);

    void registerMemberToFlat(String flatNumber, UUID societyId, UUID societyMemberId);

    void saveFlatsFromExcel(MultipartFile file, UUID societyId) throws IOException, SocietyNotFoundException;
}
