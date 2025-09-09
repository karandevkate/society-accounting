package com.fqts.mysociety.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlatRegistrationRequest {
    private String flatNumber;
    private UUID societyId;
    private UUID societyMemberId;

}