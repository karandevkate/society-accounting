package com.fqts.mysociety.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SocietyRegistrationDto {
    private String mobile;
    private String fullName;
    private String alternateMobile;
    private String email;
    private String alternateEmail;
    private String nativeAddress;
    private String nativeMobile;
    private String password;
    private String govId;

    private String societyName;
    private String registrationNumber;
    private String detailedAddress;
    private String landmark;
    private String zone;
    private String pincode;
    private String state;
    private String city;
}
