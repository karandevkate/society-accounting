package com.fqts.mysociety.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class RegisterPropertyRequestDTO {
    private UUID memberId;
    private String flatNumber;
    private UUID societyId;
}

