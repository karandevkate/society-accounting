package com.fqts.mysociety.dto.response;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class JournalResponseDTO {

    private UUID journalId;
    private double debitAmount;
    private double creditAmount;
    private Date valueDate;
    private Date transactionDate;
    private String narration;
    private String comments;
    private UUID collectionId;
    private UUID matrixChargesId;
    private String flatNumber;
    private UUID societyId;
}
