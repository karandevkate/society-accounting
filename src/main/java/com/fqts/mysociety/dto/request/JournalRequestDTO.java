package com.fqts.mysociety.dto.request;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class JournalRequestDTO {

    private double debitAmount;
    private double creditAmount;
    private Date dueDate;
    private Date transactionDate;
    private Date valueDate;
    private String narration;
    private UUID collectionId;
    private UUID matrixChargesId;
    private String flatNumber;
    private UUID societyId;
    private String comments;
}
