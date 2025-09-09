package com.fqts.mysociety.service;

import com.fqts.mysociety.dto.response.SocietyMemberListDTO;
import com.fqts.mysociety.model.FlatId;

import java.util.List;
import java.util.UUID;

public interface MemberMasterService {
//    SocietyMember registerForSociety(SocietyRegistrationRequestDTO requestDTO);
//
//    SocietyMember approveMember(UUID memberMasterId, Role role);

    List<SocietyMemberListDTO> getMembersBySocietyId(UUID societyId);

    List<SocietyMemberListDTO> getPendingMembers(UUID societyId);

    List<SocietyMemberListDTO> getApprovedOrAutoApprovedMembers(UUID societyId);


    void updateLastVisited(UUID memberId, UUID societyId, FlatId flatId) ;

    }

