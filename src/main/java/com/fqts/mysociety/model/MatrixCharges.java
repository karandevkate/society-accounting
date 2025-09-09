package com.fqts.mysociety.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "matrix_charges")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatrixCharges {

  @Id
  @Column(name = "matrix_charges_id", updatable = false, nullable = false)
  private UUID matrixChargesId = UUID.randomUUID();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "society_id", nullable = false, insertable = false, updatable = false)
  private Society society;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
          @JoinColumn(name = "flat_number", referencedColumnName = "flat_number", nullable = false),
          @JoinColumn(name = "society_id", referencedColumnName = "society_id", nullable = false)
  })
  @JsonBackReference
  private Flat flat;

  @Enumerated(EnumType.STRING)
  @Column(name = "charges_type")
  private ChargesType chargesType;

  @Column(name = "description")
  private String description;

  @Column(name = "flat_type")
  private String flatType;

  @Column(name = "amount")
  private Double amount;

  @Column(name = "interest")
  private double interest;

  @Enumerated(EnumType.STRING)
  @Column(name = "payment_frequency")
  private PaymentFrequency paymentFrequency;

  @OneToMany(mappedBy = "matrixCharges", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonManagedReference
  private List<BillCollection> billCollections;
}