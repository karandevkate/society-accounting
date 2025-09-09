package com.fqts.mysociety.dto.response;

import lombok.*;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StaffSalaryResponseDto {
    private UUID SalaryId;
    private String name;
    private String contact;
    private String email;
    private String transactionId;
    private Double amount;
    private Date paymentDate;
    private String description;
}
