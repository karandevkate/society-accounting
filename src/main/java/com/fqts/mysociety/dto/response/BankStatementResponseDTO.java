package com.fqts.mysociety.dto.response;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class BankStatementResponseDTO {
    private UUID statementId;
    private Date uploadedDate;
    private String uploadedBy;
    private byte[] filePath;
    private String bankName;
    private Date statementStartDate;
    private Date statementEndDate;
}