package com.fqts.mysociety.dto.response;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StaffResponseDto {
    private UUID staffId;
    private String name;
    private String govIdNumber;
    private String govIdPhoto;
    private String staffPhoto;
    private String contact;
    private String email;
    private String permanentAddress;
    private String currentAddress;
    private String role;
    private Double salary;
    private Date joiningDate;
    private Boolean status;
    private String societyName;
}
