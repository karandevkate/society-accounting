package com.fqts.mysociety.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "society_expense")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocietyExpense {

  @Id
  private UUID expenseId = UUID.randomUUID();

  @Column(name = "expense_type", nullable = false)
  private String expenseType;

  @Column(name = "expense_date", nullable = false)
  private LocalDate expenseDate;

  @Column(name = "total_amount", nullable = false)
  private Float totalAmount;

  @Column(name = "description", length = 255)
  private String description;

  @Temporal(TemporalType.DATE)
  @Column()
  private Date createdAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "society_id", nullable = false)
  private Society society;
}
