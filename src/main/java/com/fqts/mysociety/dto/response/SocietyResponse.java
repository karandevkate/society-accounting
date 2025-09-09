package com.fqts.mysociety.dto.response;

import com.fqts.mysociety.model.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class SocietyResponse {
    private UUID societyId;
    private String societyName;
    private String registrationNumber;
    private String detailedAddress;
    private String state;
    private String city;
    private String landmark;
    private String zone;
    private String pincode;
    private Date registeredAt = new Date();
    private Status status;
}
