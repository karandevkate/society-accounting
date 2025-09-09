package com.fqts.mysociety.dto.response;

import com.fqts.mysociety.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SocietyMemberListDTO {
    private String flatNumber;
    private String flat;
    private String wing;
    private String flatType;
    private boolean isSelfOccupied;

    private UUID societyId;
    private String societyName;
    private String registrationNumber;


    private UUID societyMemberId;
    private boolean status;
    private Role role;

    private UUID memberId;
    private String memberName;
    private String mobile;
    private String alternateMobile;
    private String email;
    private String alternateEmail;
    private String nativeAddress;
    private String nativeMobile;
    private String govId;
    private Date createdAt;


}
