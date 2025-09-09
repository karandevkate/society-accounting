package com.fqts.mysociety.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class SocietyWithAdminResponseDto {
    private UUID societyId;
    private String societyName;
    private String registrationNo;
    private String detailedAddress;
    private String state;
    private String city;
    private String zone;
    private String landmark;
    private String pincode;
    private String status;
    private List<SocietyAdminDto> societyAdminDto;
}
