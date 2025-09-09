package com.fqts.mysociety.service.impl;

import com.fqts.mysociety.dto.request.MemberLoginRequestDTO;
import com.fqts.mysociety.dto.request.MemberSignupRequestDTO;
import com.fqts.mysociety.dto.request.MemberSocietyRegistrationDTO;
import com.fqts.mysociety.dto.request.RegisterPropertyRequestDTO;
import com.fqts.mysociety.dto.response.MemberLoginResponseDTO;
import com.fqts.mysociety.dto.response.MemberResponseDTO;
import com.fqts.mysociety.dto.response.SocietyMemberResponseDto;
import com.fqts.mysociety.exception.*;
import com.fqts.mysociety.model.*;
import com.fqts.mysociety.repository.FlatRepository;
import com.fqts.mysociety.repository.MemberRepository;
import com.fqts.mysociety.repository.SocietyMemberRepository;
import com.fqts.mysociety.repository.SocietyRepository;
import com.fqts.mysociety.service.EmailService;
import com.fqts.mysociety.service.MemberService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {


  private static final Logger log = LoggerFactory.getLogger(MemberServiceImpl.class);

  private final MemberRepository memberRepository;
  private final SocietyMemberRepository societyMemberRepository;
  private final SocietyRepository societyRepository;
  private final EmailService emailService;
  private final FlatRepository flatRepository;

  @Override
  public Member signup(MemberSignupRequestDTO signupRequestDTO) {
    if (memberRepository.existsByMobile(signupRequestDTO.getMobile())) {
      throw new MobileAlreadyRegisteredException("Mobile number already registered.");
    }

    Member member = new Member();
    member.setMobile(signupRequestDTO.getMobile());
    member.setFullName(signupRequestDTO.getFullName());
    member.setAlternateMobile(signupRequestDTO.getAlternateMobile());
    member.setEmail(signupRequestDTO.getEmail());
    member.setAlternateEmail(signupRequestDTO.getAlternateEmail());
    member.setNativeAddress(signupRequestDTO.getNativeAddress());
    member.setNativeMobile(signupRequestDTO.getNativeMobile());
    member.setPassword(signupRequestDTO.getPassword());
    member.setGovId(signupRequestDTO.getGovId());


    String token = UUID.randomUUID().toString();
    member.setActivationToken(token);
    member.setActivated(false);

    Member savedMember = memberRepository.save(member);

    emailService.sendActivationEmail(savedMember.getEmail(), savedMember.getFullName(), token);

    return savedMember;
  }

  @Override
  public MemberResponseDTO getMemberByMemberId(UUID memberId) {
    log.info("Fetching member with ID: {}", memberId);

    Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> {
              log.error("Member not found for ID: {}", memberId);
              return new MemberNotFoundException("Member not found for ID: " + memberId);
            });

    MemberResponseDTO responseDTO = new MemberResponseDTO(
            member.getMemberId(),
            member.getFullName(),
            member.getMobile(),
            member.getEmail(),
            member.getAlternateMobile(),
            member.getAlternateEmail(),
            member.getNativeAddress(),
            member.getNativeMobile(),
            member.getGovId()
    );

    log.debug("Member details retrieved: {}", responseDTO);
    return responseDTO;
  }

  @Override
  public Optional<Member> findByActivationToken(String token) {
    return memberRepository.findByActivationToken(token);
  }

  @Override
  public Member saveMember(Member member) {
    return memberRepository.save(member);
  }

  @Override
  public MemberLoginResponseDTO login(MemberLoginRequestDTO loginRequestDTO) {
    Logger log = LoggerFactory.getLogger(getClass());
    log.info("Login attempt for mobile: {}", loginRequestDTO.getMobile());

    Member member = memberRepository.findByMobile(loginRequestDTO.getMobile())
            .orElseThrow(() -> new RuntimeException("Invalid credentials"));

    if (!member.isActivated()) {
      String token = UUID.randomUUID().toString();
      member.setActivationToken(token);
      memberRepository.save(member);
      emailService.sendActivationEmail(member.getEmail(), member.getFullName(), token);
      throw new RuntimeException("Account is not activated. Check your email.");
    }

    if (!member.getPassword().equals(loginRequestDTO.getPassword())) {
      throw new RuntimeException("Invalid credentials");
    }

    List<SocietyMember> societyMemberList = societyMemberRepository.findAllByMember_MemberId(member.getMemberId());
    if (societyMemberList.isEmpty()) {
      log.info("Login successful for member ID={} but no society linked", member.getMemberId());
      return new MemberLoginResponseDTO(
              member.getMemberId(),
              member.getFullName(),
              member.getMobile(),
              member.getEmail(),
              null,
              null,
              null,  // no flat
              null   // no societyMemberId
      );
    }

    UUID lastSocietyId = member.getLastVisitedSocietyId();
    if (lastSocietyId == null) {
      lastSocietyId = societyMemberList.get(0).getSociety().getSocietyId();

      member.setLastVisitedSocietyId(lastSocietyId);
      memberRepository.save(member);
    }

    SocietyMember selectedSm = null;
    for (SocietyMember sm : societyMemberList) {
      if (sm.getSociety().getSocietyId().equals(lastSocietyId)) {
        selectedSm = sm;
        break;
      }
    }
    if (selectedSm == null) {
      selectedSm = societyMemberList.get(0);
    }

    UUID societyMemberId = selectedSm.getSocietyMemberId();
    Role role = selectedSm.getRole(); // role is society-level
    String lastFlatNumber = selectedSm.getLastVisitedFlatNumber();

    if (lastFlatNumber == null && selectedSm.getFlats() != null && !selectedSm.getFlats().isEmpty()) {
      lastFlatNumber = selectedSm.getFlats().get(0).getId().getFlatNumber();

      selectedSm.setLastVisitedFlatNumber(lastFlatNumber);
      societyMemberRepository.save(selectedSm);
    }

    log.info("Login successful for member ID={}", member.getMemberId());

    return new MemberLoginResponseDTO(
            member.getMemberId(),
            member.getFullName(),
            member.getMobile(),
            member.getEmail(),
            role,
            lastSocietyId,
            lastFlatNumber,
            societyMemberId
    );
  }






  @Override
  public List<SocietyMemberResponseDto> getSocietiesByMember(UUID memberId) {
    List<SocietyMember> societyMembers = societyMemberRepository.findByMemberMemberId(memberId);
    List<SocietyMemberResponseDto> societyMemberResponseDtos = new ArrayList<>();

    for (SocietyMember societyMember : societyMembers) {
      SocietyMemberResponseDto dto = new SocietyMemberResponseDto();
      dto.setSocietyMemberId(societyMember.getSocietyMemberId());
      dto.setSocietyId(societyMember.getSociety().getSocietyId());
      dto.setSocietyName(societyMember.getSociety().getSocietyName());
      dto.setSocietyUniqueCode(societyMember.getSociety().getSocietyUniqueCode());
      dto.setRole(societyMember.getRole().name());
      dto.setLastVisitedFlatNumber(societyMember.getLastVisitedFlatNumber());
      societyMemberResponseDtos.add(dto);
    }

    return societyMemberResponseDtos;
  }


  @Override
  public void registerMemberForSociety(MemberSocietyRegistrationDTO requestDTO) {
    Member member = memberRepository.findById(requestDTO.getMemberId())
        .orElseThrow(() -> new MemberNotFoundException("Member not found"));

    Society society = societyRepository.findById(requestDTO.getSocietyId())
        .orElseThrow(() -> new MemberNotFoundException("Society not found"));

    SocietyMember societyMember = new SocietyMember();
    societyMember.setMember(member);
    societyMember.setSociety(society);
    societyMember.setStatus(Status.PENDING);

    societyMemberRepository.save(societyMember);
  }

  @Override
  @Transactional
  public void registerPropertyForMember(RegisterPropertyRequestDTO requestDTO) {
    log.info("Starting property registration for memberId [{}], societyId [{}], flatNumber [{}]",
            requestDTO.getMemberId(), requestDTO.getSocietyId(), requestDTO.getFlatNumber());

    Member member = memberRepository.findById(requestDTO.getMemberId())
            .orElseThrow(() -> {
              log.error("Member [{}] not found", requestDTO.getMemberId());
              return new MemberNotFoundException("Member not found");
            });
    log.info("Found member [{}]", member.getMemberId());


    Society society = societyRepository.findById(requestDTO.getSocietyId())
            .orElseThrow(() -> {
              log.error("Society [{}] not found", requestDTO.getSocietyId());
              return new SocietyNotFoundException("Society not found");
            });
    log.info("Found society [{}]", society.getSocietyId());

    SocietyMember societyMember = societyMemberRepository
            .findApprovedMember(member.getMemberId(), society.getSocietyId())
            .orElseGet(() -> {
              log.info("No existing membership found, creating new SocietyMember for member [{}] in society [{}]",
                      member.getMemberId(), society.getSocietyId());
              SocietyMember newMember = new SocietyMember();
              newMember.setMember(member);
              newMember.setSociety(society);
              newMember.setStatus(Status.PENDING);
              return societyMemberRepository.save(newMember);
            });

    log.info("Using societyMemberId [{}]", societyMember.getSocietyMemberId());

    FlatId flatId = new FlatId(requestDTO.getFlatNumber(), requestDTO.getSocietyId());
    Flat flat = flatRepository.findById(flatId)
            .orElseThrow(() -> {
              log.error("Flat [{}] in society [{}] not found", requestDTO.getFlatNumber(), requestDTO.getSocietyId());
              return new RuntimeException("Flat not found in society");
            });
    log.info("Found flat [{}] in society [{}]", flat.getId().getFlatNumber(), flat.getId().getSocietyId());

    if (flat.getSocietyMember() != null) {
      throw new FlatAlreadyAssignedException(
              "Flat " + requestDTO.getFlatNumber() + " in society " + " is already assigned."
      );
    }

    flat.setSocietyMember(societyMember);
    flatRepository.save(flat);

    log.info("Successfully assigned flat [{}] to societyMemberId [{}]",
            flat.getId().getFlatNumber(), societyMember.getSocietyMemberId());
  }



}