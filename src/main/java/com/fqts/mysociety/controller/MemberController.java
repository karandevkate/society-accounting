package com.fqts.mysociety.controller;

import com.fqts.mysociety.dto.request.MemberLoginRequestDTO;
import com.fqts.mysociety.dto.request.MemberSignupRequestDTO;
import com.fqts.mysociety.dto.request.MemberSocietyRegistrationDTO;
import com.fqts.mysociety.dto.request.RegisterPropertyRequestDTO;
import com.fqts.mysociety.dto.response.FlatResponseDTO;
import com.fqts.mysociety.dto.response.MemberLoginResponseDTO;
import com.fqts.mysociety.dto.response.MemberResponseDTO;
import com.fqts.mysociety.dto.response.SocietyMemberResponseDto;
import com.fqts.mysociety.model.Member;
import com.fqts.mysociety.service.MemberService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.fqts.mysociety.service.impl.MemberServiceImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

  private final MemberService memberService;
  private static final Logger log = LoggerFactory.getLogger(MemberController.class);

  @PostMapping("/signup")
  public ResponseEntity<MemberResponseDTO> signup(@RequestBody MemberSignupRequestDTO signupDTO) {
    Member member = memberService.signup(signupDTO);
    MemberResponseDTO responseDTO = new MemberResponseDTO(
        member.getMemberId(),
        member.getMobile(),
        member.getFullName(),
        member.getAlternateMobile(),
        member.getEmail(),
        member.getAlternateEmail(),
        member.getNativeAddress(),
        member.getNativeMobile(),
        member.getGovId()
    );
    return ResponseEntity.ok(responseDTO);
  }

    @GetMapping("/{memberId}")
    public ResponseEntity<MemberResponseDTO> getMember(@PathVariable UUID memberId) {
        log.info("Received request to fetch member with ID: {}", memberId);
        MemberResponseDTO response = memberService.getMemberByMemberId(memberId);
        return ResponseEntity.ok(response);
    }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody MemberLoginRequestDTO loginRequestDTO) {
    try {
      MemberLoginResponseDTO responseDTO = memberService.login(loginRequestDTO);
      return ResponseEntity.ok(responseDTO);
    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }
  }

  @GetMapping("/activate")
  public ResponseEntity<String> activateAccount(@RequestParam("token") String token) {
    Optional<Member> optionalMember = memberService.findByActivationToken(token);

    if (optionalMember.isEmpty()) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid activation token.");
    }

    Member member = optionalMember.get();
    member.setActivated(true);
    member.setActivationToken(null); // clear token
    memberService.saveMember(member);

    return ResponseEntity.ok("Your account has been activated successfully.");
  }


  @GetMapping("/{memberId}/societies")
  public ResponseEntity<List<SocietyMemberResponseDto>> getMemberSocieties(
      @PathVariable UUID memberId) {
    List<SocietyMemberResponseDto> societies = memberService.getSocietiesByMember(memberId);
    return ResponseEntity.ok(societies);
  }

  @PostMapping("/register-society")
  public ResponseEntity<String> registerMemberForSociety(
      @RequestBody MemberSocietyRegistrationDTO requestDTO) {
    memberService.registerMemberForSociety(requestDTO);
    return ResponseEntity.ok(
        "Member registered for society successfully. Awaiting admin approval.");
  }



  @PostMapping("/registerproperty")
  public ResponseEntity<String> registerPropertyForMember(@RequestBody RegisterPropertyRequestDTO requestDTO) {
    memberService.registerPropertyForMember(requestDTO);
    return ResponseEntity.ok("Property registered successfully!");
  }
}
