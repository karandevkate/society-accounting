package com.fqts.mysociety.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "society")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Society {

  @Id
  @Column(name = "society_id", updatable = false, nullable = false)
  private UUID societyId = UUID.randomUUID();

  private String societyName;
  private String registrationNumber;
  private String societyUniqueCode;
  private String detailedAddress;
  private String landmark;
  private String zone;
  private String pincode;
  private Boolean isVerfied;
  private String city;
  private String states;
  private Date registeredAt = new Date();

  @Enumerated(EnumType.STRING)
  private Status status;

  @OneToMany(mappedBy = "society", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonManagedReference
  private List<SocietyMember> societyMembers;

  @OneToMany(mappedBy = "society", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Flat> flats;

  @OneToMany(mappedBy = "society", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonManagedReference
  private List<Staff> staffList;

  @OneToMany(mappedBy = "society", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonManagedReference
  private List<MaintenanceCharges> maintenanceChargesList;

  @OneToMany(mappedBy = "society", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonManagedReference
  private List<StaffSalary> staffSalaries;

    @OneToMany(mappedBy = "society", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<SocietyExpense> societyExpenses;

    @OneToMany(mappedBy = "society", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Journal> journalList;

    @OneToMany(mappedBy = "society", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<BillCollection> billCollectionList;

}
