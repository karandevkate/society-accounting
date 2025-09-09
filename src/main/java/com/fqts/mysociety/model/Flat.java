package com.fqts.mysociety.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "flat")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Flat {

  @EmbeddedId
  private FlatId id;

  private String flat;
  private String wing;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "society_member_id")
  private SocietyMember societyMember;

  @ManyToOne(fetch = FetchType.LAZY)
  @MapsId("societyId")
  @JoinColumn(name = "society_id", referencedColumnName = "society_id", nullable = false)
  private Society society;

  @Column(name = "flat_type")
  private String flatType;

  @Column(name = "is_self_occupied")
  private boolean isSelfOccupied;

  @OneToMany(mappedBy = "flat", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JsonManagedReference
  private List<MaintenanceCharges> maintenanceCharges;

  @OneToMany(mappedBy = "flat", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JsonManagedReference
  private List<Journal> journalList;

  @OneToMany(mappedBy = "flat", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<MatrixCharges> matrixCharges;

  @OneToMany(mappedBy = "flat", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JsonManagedReference
  private List<BillCollection> billCollections;


}

