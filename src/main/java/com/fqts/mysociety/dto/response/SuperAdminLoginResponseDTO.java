package com.fqts.mysociety.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SuperAdminLoginResponseDTO {
    private UUID superAdminId;
    private String fullName;
    private String mobile;
    private String email;
    private String role;
}
