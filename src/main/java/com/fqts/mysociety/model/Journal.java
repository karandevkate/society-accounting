package com.fqts.mysociety.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "journal")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Journal {

    @Id
    @Column(name = "journal_id", updatable = false, nullable = false)
    private UUID journalId = UUID.randomUUID();

    @Column(name = "debit_amount")
    private double debitAmount;

    @Column(name = "credit_amount")
    private double creditAmount;

    @Temporal(TemporalType.DATE)
    @Column(name = "transaction_date")
    private Date transactionDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "value_date")
    private Date valueDate;

    @Column(name = "narration")
    private String narration;

    @Column(name = "comments")
    private String comments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_collection_id", nullable = false)
    private BillCollection billCollection;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "flat_number", referencedColumnName = "flat_number"),
            @JoinColumn(name = "society_id", referencedColumnName = "society_id")
    })
    @JsonBackReference
    private Flat flat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matrix_charges_id", nullable = false)
    private MatrixCharges matrixCharges;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "society_id", nullable = false, insertable = false, updatable = false)
    private Society society;
}

