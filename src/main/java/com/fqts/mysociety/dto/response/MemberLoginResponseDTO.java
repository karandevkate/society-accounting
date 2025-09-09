package com.fqts.mysociety.dto.response;

import com.fqts.mysociety.model.FlatId;
import com.fqts.mysociety.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberLoginResponseDTO {
    private UUID memberId;
    private String fullName;
    private String mobile;
    private String email;
    private Role role;

    private UUID lastVisitedSocietyId;
    private String lastVisitedFlatNumber;
    private UUID societyMemberId;
}

