package com.fqts.mysociety.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "staff_salary")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StaffSalary {

    @Id
    @Column(name = "salary_id", updatable = false, nullable = false)
    private UUID salaryId = UUID.randomUUID();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id", nullable = false)
    private Staff staff;

    @Column(nullable = false)
    private Double amount; // Salary amount paid

    @Column()
    private String transactionId; // Transaction ID or UTR number (e.g., for RTGS)

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date paymentDate; // Date of salary payment

    @Column(length = 500)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "society_id", nullable = false)
    private Society society;
}