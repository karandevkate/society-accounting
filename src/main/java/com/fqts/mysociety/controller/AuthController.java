package com.fqts.mysociety.controller;

import com.fqts.mysociety.dto.request.MemberLoginRequestDTO;
import com.fqts.mysociety.dto.request.MemberSignupRequestDTO;
import com.fqts.mysociety.dto.response.MemberLoginResponseDTO;
import com.fqts.mysociety.dto.response.MemberResponseDTO;
import com.fqts.mysociety.model.Member;
import com.fqts.mysociety.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;

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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberLoginRequestDTO loginRequestDTO) {
        try {
            MemberLoginResponseDTO responseDTO = memberService.login(loginRequestDTO);
            return ResponseEntity.ok(responseDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
