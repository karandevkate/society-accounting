package com.fqts.mysociety.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SocietyRegistrationRequestDTO {
    private UUID memberId;
    private UUID societyId;
    private String flatNumber;
}

