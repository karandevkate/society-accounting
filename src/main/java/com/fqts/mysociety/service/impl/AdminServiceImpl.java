package com.fqts.mysociety.service.impl;


import com.fqts.mysociety.model.Role;
import com.fqts.mysociety.model.SocietyMember;
import com.fqts.mysociety.model.Status;
import com.fqts.mysociety.repository.MemberRepository;
import com.fqts.mysociety.repository.SocietyMemberRepository;
import com.fqts.mysociety.service.AdminService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

  private final SocietyMemberRepository societyMemberRepository;
  private final MemberRepository memberRepository;

  @Override
  public void approveMember(UUID societyMemberId, Role role) {
    SocietyMember societyMember = societyMemberRepository.findById(societyMemberId)
        .orElseThrow(() -> new RuntimeException("Member not found"));
    log.info("Society member id: " + societyMember.getSocietyMemberId());
    societyMember.setStatus(Status.APPROVED);
    societyMember.setRole(role);

    societyMemberRepository.save(societyMember);
  }

  @Override
  public void rejectMember(UUID societyMemberId, Role role) {
    SocietyMember societyMember = societyMemberRepository.findById(societyMemberId)
            .orElseThrow(() -> new RuntimeException("Member not found"));
    log.info("Society member id: " + societyMember.getSocietyMemberId());
    societyMember.setStatus(Status.REJECTED);
    societyMember.setRole(role);

    societyMemberRepository.save(societyMember);
  }


}
