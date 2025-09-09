package com.fqts.mysociety.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "bank_statement")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BankStatement {

  @Id
  @Column(name = "bank_statement_id", updatable = false, nullable = false)
  private UUID bankStatementId;

  @ManyToOne
  @JoinColumn(name = "incoming_file_id", nullable = false)
  private IncomingFile incomingFile;

  @Temporal(TemporalType.DATE)
  @Column(name = "transaction_date")
  private Date transactionDate;

  @Temporal(TemporalType.DATE)
  @Column(name = "value_date")
  private Date valueDate;

  @Column(name = "description", columnDefinition = "TEXT")
  private String description;

  @Column(name = "chq_Ref_Number")
  private String chequeNumber;

  @Column(name = "debit_amount", precision = 19, scale = 2)
  private BigDecimal debitAmount;

  @Column(name = "credit_amount", precision = 19, scale = 2)
  private BigDecimal creditAmount;

  @Column(name = "balance", precision = 19, scale = 2)
  private BigDecimal balance;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "processed_at")
  private Date processedAt;

  @Column(unique = true)
  private String transactionHash;
}
