package com.fqts.mysociety.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fqts.mysociety.model.PaymentMode;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class BillCollectionRequestDTO {
    @NotNull(message = "Matrix charges ID is required")
    private UUID matrixChargesId = UUID.randomUUID();

    @NotNull(message = "Flat number is required")
    @JsonProperty("flatNumber")
    private String flatNumber;

    @NotNull(message = "Paid amount is required")
    @JsonProperty("paidAmount")
    private Double paidAmount;

    @NotNull(message = "Paid date is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Date paidDate;

    @NotNull(message = "Payment mode is required")
    private PaymentMode paymentMode;

    private String transactionRefNumber;
    private UUID societyId;
//
//    @NotNull(message = "Bank statement ID is required")
//    private UUID statementId;


    @Override
    public String toString() {
        return "BillCollectionRequestDTO{" +
                "matrixChargesId=" + matrixChargesId +
                ", flatNumber='" + flatNumber + '\'' +
                ", paidAmount=" + paidAmount +
                ", paidDate=" + paidDate +
                ", paymentMode=" + paymentMode +
                ", transactionRefNumber='" + transactionRefNumber + '\'' +
                ", societyId=" + societyId +
                '}';
    }
}