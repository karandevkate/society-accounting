package com.fqts.mysociety.dto.request;

import lombok.Data;

import java.util.Date;

@Data
public class BankStatementRequestDTO {
    private Date uploadedDate;
    private String uploadedBy;
    private byte[] filePath;
    private String bankName;
    private Date statementStartDate;
    private Date statementEndDate;
}