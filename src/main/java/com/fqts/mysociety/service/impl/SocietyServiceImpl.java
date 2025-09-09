package com.fqts.mysociety.service.impl;

import com.fqts.mysociety.dto.request.MemberSignupRequestDTO;
import com.fqts.mysociety.dto.request.SocietyRegistrationDto;
import com.fqts.mysociety.dto.request.SocietyRequest;
import com.fqts.mysociety.dto.response.SocietyAdminDto;
import com.fqts.mysociety.dto.response.SocietyDetailsWithCountsResponse;
import com.fqts.mysociety.dto.response.SocietyResponse;
import com.fqts.mysociety.dto.response.SocietyWithAdminResponseDto;
import com.fqts.mysociety.exception.InvalidSocietyRequestException;
import com.fqts.mysociety.exception.SocietyNotFoundException;
import com.fqts.mysociety.model.Member;
import com.fqts.mysociety.model.Role;
import com.fqts.mysociety.model.Society;
import com.fqts.mysociety.model.SocietyMember;
import com.fqts.mysociety.model.Status;
import com.fqts.mysociety.repository.MemberRepository;
import com.fqts.mysociety.repository.SocietyMemberRepository;
import com.fqts.mysociety.repository.SocietyRepository;
import com.fqts.mysociety.service.EmailService;
import com.fqts.mysociety.service.MemberService;
import com.fqts.mysociety.service.SocietyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.fqts.mysociety.helper.MessageConstants.SOCIETY_NOT_FOUND_WITH_ID;


@Service
public class SocietyServiceImpl implements SocietyService {

  private static final Logger log = LoggerFactory.getLogger(SocietyServiceImpl.class);
  private final SocietyRepository societyRepository;
  private final EmailService emailService;
  private final MemberService memberService;
  private final MemberRepository memberRepository;
  private final SocietyMemberRepository societyMemberRepository;

  public SocietyServiceImpl(SocietyRepository societyRepository, EmailService emailService,
      MemberService memberService, MemberRepository memberRepository,
      SocietyMemberRepository societyMemberRepository) {
    this.societyRepository = societyRepository;
    this.emailService = emailService;
    this.memberService = memberService;
    this.memberRepository = memberRepository;
    this.societyMemberRepository = societyMemberRepository;
  }

  @Override
  public SocietyResponse getSocietyById(UUID societyId) {
    log.info("Fetching society details for ID: {}", societyId);
    Society society = societyRepository.findById(societyId)
            .orElseThrow(() -> {
              log.error("Society not found: ID {}", societyId);
              return new SocietyNotFoundException(societyId);
            });
    return new SocietyResponse(
            society.getSocietyId(),
            society.getSocietyName(),
            society.getRegistrationNumber(),
            society.getDetailedAddress(),
            society.getStates(), // Use direct field from Society
            society.getCity(),
            society.getLandmark(),
            society.getZone(),
            society.getPincode(),
            society.getRegisteredAt(),
            society.getStatus()
    );
  }

  @Override
  public Society updateSociety(UUID societyId, SocietyRequest request)
      throws SocietyNotFoundException {
    log.info("Updating society with ID: {}", societyId);

    Society society = societyRepository.findById(societyId)
        .orElseThrow(() -> new SocietyNotFoundException("Society not found"));

    society.setSocietyName(request.getSocietyName());
    society.setRegistrationNumber(request.getRegistrationNumber());
    society.setDetailedAddress(request.getDetailedAddress());
    society.setLandmark(request.getLandmark());
    society.setZone(request.getZone());
    society.setPincode(request.getPincode());

    Society updatedSociety = societyRepository.save(society);
    log.info("Society updated successfully: ID {}", updatedSociety.getSocietyId());

    return updatedSociety;
  }

  @Override
  public String approveSociety(UUID societyId) {
    log.info("Approving society with ID: {}", societyId);
    Society society = societyRepository.findById(societyId)
        .orElseThrow(() -> {
          log.error("Society not found: ID {}", societyId);
          return new SocietyNotFoundException(SOCIETY_NOT_FOUND_WITH_ID + societyId);
        });
    society.setStatus(Status.APPROVED);
    Society updatedSociety = societyRepository.save(society);
    log.info("Society approved successfully: ID {}, Status={}", updatedSociety.getSocietyId(),
        updatedSociety.getStatus());
    return "Society with ID " + updatedSociety.getSocietyId() + " has been approved  successfully.";
  }

  @Override
  public String rejectSociety(UUID societyId) {
    log.info("Rejecting society with ID: {}", societyId);
    Society society = societyRepository.findById(societyId)
        .orElseThrow(() -> {
          log.error("Society not found: ID {}", societyId);
          return new SocietyNotFoundException(SOCIETY_NOT_FOUND_WITH_ID + societyId);
        });
    society.setStatus(Status.REJECTED);
    Society updatedSociety = societyRepository.save(society);
    log.info("Society rejected successfully: ID {}, Status={}", updatedSociety.getSocietyId(),
        updatedSociety.getStatus());
    return "Society with ID " + updatedSociety.getSocietyId() + " has been rejected successfully.";
  }

  @Override
  public UUID registerSocietyWithAdmin(SocietyRegistrationDto societyRegistrationDto)
      throws InvalidSocietyRequestException {
    MemberSignupRequestDTO member = new MemberSignupRequestDTO();
    member.setMobile(societyRegistrationDto.getMobile());
    member.setFullName(societyRegistrationDto.getFullName());
    member.setAlternateMobile(societyRegistrationDto.getAlternateMobile());
    member.setEmail(societyRegistrationDto.getEmail());
    member.setAlternateEmail(societyRegistrationDto.getAlternateEmail());
    member.setNativeAddress(societyRegistrationDto.getNativeAddress());
    member.setNativeMobile(societyRegistrationDto.getNativeMobile());
    member.setPassword(societyRegistrationDto.getPassword());
    member.setGovId(societyRegistrationDto.getGovId());

    memberService.signup(member);

    Member savedMember = memberRepository.findByMobile(societyRegistrationDto.getMobile())
        .orElseThrow(() -> new RuntimeException("Member creation failed"));

    Society society = new Society();
    society.setSocietyName(societyRegistrationDto.getSocietyName());
    society.setRegistrationNumber(societyRegistrationDto.getRegistrationNumber());

        society.setSocietyUniqueCode(generateSocietyUniqueCode(societyRegistrationDto.getSocietyName()));

    society.setDetailedAddress(societyRegistrationDto.getDetailedAddress());
    society.setLandmark(societyRegistrationDto.getLandmark());
    society.setZone(societyRegistrationDto.getZone());
    society.setPincode(societyRegistrationDto.getPincode());
    society.setStatus(Status.AUTOAPPROVED);
    society.setStates(societyRegistrationDto.getState());
    society.setCity(societyRegistrationDto.getCity());

    society = societyRepository.save(society);

    SocietyMember societyMember = new SocietyMember();
    societyMember.setMember(savedMember);
    societyMember.setSociety(society);
    societyMember.setStatus(Status.AUTOAPPROVED);

    societyMember.setRole(Role.ADMIN);

    societyMemberRepository.save(societyMember);
    emailService.sendSuperAdminNotification(societyMember);
    return society.getSocietyId();
  }

  private String generateSocietyUniqueCode(String societyName) {
    log.debug("Generating unique code for society: {}", societyName);

    String[] words = societyName.trim().split("\\s+");
    StringBuilder initials = new StringBuilder();
    for (String word : words) {
      if (!word.isEmpty()) {
        initials.append(Character.toUpperCase(word.charAt(0)));
      }
    }

    String prefix = initials.toString();
    log.debug("Extracted initials: {}", prefix);

    String lastCode = societyRepository.findLastSocietyCode();
    log.debug("Last society code in database: {}", lastCode);

    int nextNumber = 1;
    if (lastCode != null) {
      String numberPart = lastCode.replaceAll("[^0-9]", "");
      try {
        nextNumber = Integer.parseInt(numberPart) + 1;
      } catch (NumberFormatException e) {
        log.warn("Failed to parse number from last code: {}", lastCode);
      }
    }

    String newCode = String.format("%s%03d", prefix, nextNumber);
    log.info("Generated new society code: {}", newCode);
    return newCode;
  }

  @Override
  public List<SocietyResponse> getAllSocieties() {
    List<Society> societies = societyRepository.findApprovedOrAutoApprovedSocieties();
    societies.sort(Comparator.comparing(Society::getRegisteredAt).reversed());

    List<SocietyResponse> societyResponseDtos = new ArrayList<>();

    for (Society society : societies) {
      SocietyResponse societyResponseDto = new SocietyResponse(
              society.getSocietyId(),
              society.getSocietyName(),
              society.getRegistrationNumber(),
              society.getDetailedAddress(),
              society.getStates(),
              society.getCity(),
              society.getLandmark(),
              society.getZone(),
              society.getPincode(),
              society.getRegisteredAt(),
              society.getStatus()
      );
      societyResponseDtos.add(societyResponseDto);
    }
    return societyResponseDtos;
  }

  @Override
  public List<Society> findByStatusAndRegisteredBefore(Status status, Date registeredBefore) {
    return societyRepository.findByStatusAndRegisteredAtBefore(status, registeredBefore);
  }

  @Override
  public Society save(Society society) {
    return societyRepository.save(society);
  }

  @Override
  public SocietyWithAdminResponseDto getSocietyWithAdmins(UUID societyId) {
    log.info("Fetching society details with admins for societyId: {}", societyId);

    Society society = societyRepository.findById(societyId)
            .orElseThrow(() -> {
              log.error("Society not found for ID: {}", societyId);
              return new SocietyNotFoundException("Society not found with ID: " + societyId);
            });

    log.debug("Society found: {}", society.getSocietyName());

    List<SocietyAdminDto> adminList = society.getSocietyMembers().stream()
            .filter(member -> member.getRole() == Role.ADMIN)
            .sorted(Comparator.comparing(member -> member.getMember().getFullName()))
            .map(member -> {
              Member m = member.getMember();
              log.debug("Mapping admin member: {}", m.getFullName());
              return new SocietyAdminDto(member.getSocietyMemberId(),m.getFullName(), m.getEmail(), m.getMobile(), member.getStatus());
            })
            .toList();

    SocietyWithAdminResponseDto responseDto = new SocietyWithAdminResponseDto(
            society.getSocietyId(),
            society.getSocietyName(),
            society.getRegistrationNumber(),
            society.getDetailedAddress(),
            society.getStates(),
            society.getCity(),
            society.getZone(),
            society.getLandmark(),
            society.getPincode(),
            society.getStatus().name(),
            adminList
    );

    log.info("Successfully built response for societyId: {}", societyId);
    return responseDto;
  }

  public SocietyDetailsWithCountsResponse getSocietyWithCounts(UUID id) {
    Society society = societyRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Society not found"));

    long pending = societyMemberRepository.countPendingMembers(id);
    long approved = societyMemberRepository.countApprovedMembers(id);

    return new SocietyDetailsWithCountsResponse(
            society.getSocietyId(),
            society.getSocietyName(),
            society.getDetailedAddress(),
            society.getCity(),
            society.getCity(),
            pending,
            approved,
            pending+approved
    );
  }

}

