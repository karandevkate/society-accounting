package com.fqts.mysociety.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StaffSalaryRequestDto {
    private UUID staffId;
    private Double amount;
    private String transactionId;
    private Date paymentDate;
    private String description;
    private UUID societyId;
}
