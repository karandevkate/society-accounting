package com.fqts.mysociety.dto.response;

import com.fqts.mysociety.model.PaymentMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillCollectionResponseDTO {
    private UUID collectionId;
    private UUID matrixChargesId;
    private String flatNumber;
    private Double paidAmount;
    private Date paidDate;
    private PaymentMode paymentMode;
    private String transactionRefNumber;
    private boolean isVerified;
    private Date verificationDate;
    private String verifiedBy;
    private UUID societyId;
    private byte[] imageUrl;

    // Custom constructor for JPQL
    public BillCollectionResponseDTO(UUID collectionId, UUID matrixChargesId, String flatNumber,
                                     Double paidAmount, Date paidDate, PaymentMode paymentMode,
                                     String transactionRefNumber, boolean isVerified, Date verificationDate,
                                     String verifiedBy, UUID societyId) {
        this.collectionId = collectionId;
        this.matrixChargesId = matrixChargesId;
        this.flatNumber = flatNumber;
        this.paidAmount = paidAmount;
        this.paidDate = paidDate;
        this.paymentMode = paymentMode;
        this.transactionRefNumber = transactionRefNumber;
        this.isVerified = isVerified;
        this.verificationDate = verificationDate;
        this.verifiedBy = verifiedBy;
        this.societyId = societyId;
    }
}
