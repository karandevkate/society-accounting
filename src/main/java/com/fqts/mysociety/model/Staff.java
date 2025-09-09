package com.fqts.mysociety.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "staff")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Staff {

    @Id
    @Column(name = "staff_id", updatable = false, nullable = false)
    private UUID staffId = UUID.randomUUID();

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String govIdNumber;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "gov_id_photo")
    @JsonIgnore
    private byte[] govIdPhoto;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "staff_photo")
    @JsonIgnore
    private byte[] staffPhoto;

    @Column(nullable = false)
    private String contact;

    private String email;

    @Column()
    private String permanentAddress;

    @Column()
    private String currentAddress;

    private String role;

    private Double salary;

    @Temporal(TemporalType.DATE)
    @Column()
    private Date joiningDate;

    @Column()
    private Boolean staffStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "society_id", nullable = false)
    private Society society;

    @OneToMany(mappedBy = "staff", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<StaffSalary> staffSalaries;
}