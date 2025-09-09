package com.fqts.mysociety.dto.response;

import com.fqts.mysociety.model.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class SocietyAdminDto {
    private UUID societyMemberId;
    private String fullName;
    private String email;
    private String mobile;
    private Status status;
}
