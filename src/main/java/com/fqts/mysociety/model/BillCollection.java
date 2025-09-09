package com.fqts.mysociety.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "bill_collection")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillCollection {

    @Id
    @GeneratedValue
    @Column(name = "collection_id", updatable = false, nullable = false)
    private UUID collectionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matrix_charges_id", nullable = false)
    private MatrixCharges matrixCharges; // FK to matrix_charges

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "flat_number", referencedColumnName = "flat_number", nullable = false),
            @JoinColumn(name = "society_id", referencedColumnName = "society_id", nullable = false)
    })
    private Flat flat;

    @Column(name = "paid_amount")
    private Double paidAmount;

    @Temporal(TemporalType.DATE)
    @Column(name = "paid_date")
    private Date paidDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_mode")
    private PaymentMode paymentMode;

    @Column(name = "transaction_ref_number")
    private String transactionRefNumber;

    @Column(name = "is_verified")
    private boolean isVerified;

    @Temporal(TemporalType.DATE)
    @Column(name = "verification_date")
    private Date verificationDate;

    @Column(name = "verified_by")
    private String verifiedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "society_id", nullable = false, insertable = false, updatable = false)
    private Society society;

//  @ManyToOne(fetch = FetchType.LAZY)
//  @JoinColumn(name = "statement_id", nullable = false)
//  private BankStatement bankStatement; // FK to BankStatement

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "image")
    private byte[] image;



}
