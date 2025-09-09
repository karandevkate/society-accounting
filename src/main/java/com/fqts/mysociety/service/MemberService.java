package com.fqts.mysociety.service;

import com.fqts.mysociety.dto.request.MemberLoginRequestDTO;
import com.fqts.mysociety.dto.request.MemberSignupRequestDTO;
import com.fqts.mysociety.dto.request.MemberSocietyRegistrationDTO;
import com.fqts.mysociety.dto.request.RegisterPropertyRequestDTO;
import com.fqts.mysociety.dto.response.MemberLoginResponseDTO;
import com.fqts.mysociety.dto.response.MemberResponseDTO;
import com.fqts.mysociety.dto.response.SocietyMemberResponseDto;
import com.fqts.mysociety.model.Member;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MemberService {

  Member signup(MemberSignupRequestDTO signupRequestDTO);

  MemberResponseDTO getMemberByMemberId(UUID memberId);

  MemberLoginResponseDTO login(MemberLoginRequestDTO loginRequestDTO);

  List<SocietyMemberResponseDto> getSocietiesByMember(UUID memberId);

  void registerMemberForSociety(MemberSocietyRegistrationDTO requestDTO);

  Optional<Member> findByActivationToken(String token);

  Member saveMember(Member member);

  void registerPropertyForMember(RegisterPropertyRequestDTO requestDTO);

}