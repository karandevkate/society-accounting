package com.fqts.mysociety.dto.request;

import com.fqts.mysociety.model.ChargesType;
import com.fqts.mysociety.model.PaymentFrequency;
import lombok.Data;

import java.util.UUID;

@Data
public class MatrixChargesDTO {
    private UUID society;
    private String flats;
    private ChargesType chargesType;
    private String description;
    private String flatType;
    private double amount;
    private double interest;
    private PaymentFrequency paymentFrequency;
}
