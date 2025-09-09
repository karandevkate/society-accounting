package com.fqts.mysociety.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StaffRequestDto {
    private String name;
    private String govIdNumber;
    private MultipartFile govIdPhoto;
    private MultipartFile staffPhoto;
    private String contact;
    private String email;
    private String permanentAddress;
    private String currentAddress;
    private String role;
    private Double salary;
    private Date joiningDate;
    private Boolean staffStatus;
    private UUID societyId;
}
