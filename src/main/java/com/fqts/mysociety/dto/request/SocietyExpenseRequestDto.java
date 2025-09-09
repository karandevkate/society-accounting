package com.fqts.mysociety.dto.request;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SocietyExpenseRequestDto {
    private String expenseType;
    private LocalDate expenseDate;
    private Float totalAmount;
    private String description;
    private UUID SocietyId;

}
