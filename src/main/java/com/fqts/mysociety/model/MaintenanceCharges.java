package com.fqts.mysociety.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.Date;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "maintenance_charges")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceCharges {

  @Id
  @Column(name = "maintenance_charge_id", updatable = false, nullable = false)
  private UUID maintenanceChargeId = UUID.randomUUID();

  @Temporal(TemporalType.DATE)
  @Column(name = "start_date")
  private Date startDate;

  @Temporal(TemporalType.DATE)
  @Column(name = "end_date")
  private Date endDate;

  @Column(name = "monthly_maintenance_amount")
  private Double monthlyMaintenanceAmount;

  @Column(name = "amount")
  private Double amount;


  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "society_id", nullable = false, insertable = false, updatable = false)
  @JsonBackReference
  private Society society;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
          @JoinColumn(name = "flat_number", referencedColumnName = "flat_number", nullable = false),
          @JoinColumn(name = "society_id", referencedColumnName = "society_id", nullable = false)
  })
  @JsonBackReference
  private Flat flat;
}

