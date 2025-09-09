package com.fqts.mysociety.dto.response;

import com.fqts.mysociety.model.ChargesType;
import com.fqts.mysociety.model.PaymentFrequency;
import lombok.Data;

import java.util.UUID;

@Data
public class MatrixChargesResponseDTO {
    private UUID matrixChargesId;
    private UUID societyId;
    private String flatNumber;
    private ChargesType chargesType;
    private String description;
    private String flatType;
    private double amount;
    private double interest;
    private PaymentFrequency paymentFrequency;
}
