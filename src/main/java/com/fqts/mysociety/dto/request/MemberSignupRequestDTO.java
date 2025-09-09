package com.fqts.mysociety.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberSignupRequestDTO {
    @NotBlank
    private String mobile;
    @NotBlank
    private String fullName;
    private String alternateMobile;
    @Email
    @NotBlank
    private String email;
    private String alternateEmail;
    private String nativeAddress;
    private String nativeMobile;
    @NotBlank
    private String password;
    private String govId;
}

