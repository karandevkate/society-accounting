package com.fqts.mysociety.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SocietyExpenseResponseDto {
    private UUID expenseId;
    private String expenseType;
    private LocalDate expenseDate;
    private Float totalAmount;
    private String description;
}
