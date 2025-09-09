package com.fqts.mysociety.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDTO {
    private UUID memberId;
    private String fullName;
    private String mobile;
    private String email;
    private String alternateMobile;
    private String alternateEmail;
    private String nativeAddress;
    private String nativeMobile;
    private String govId;

}

