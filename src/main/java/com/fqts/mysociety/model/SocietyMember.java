package com.fqts.mysociety.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "society_member")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SocietyMember {

  @Id
  @Column(name = "society_member_id", updatable = false, nullable = false)
  private UUID societyMemberId = UUID.randomUUID();

  @ManyToOne
  @JoinColumn(name = "society_id", nullable = false)
  private Society society;

  @ManyToOne
  @JoinColumn(name = "member_id", nullable = false)
  private Member member;

  @OneToMany(mappedBy = "societyMember", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private List<Flat> flats;

  @Enumerated(EnumType.STRING)
  private Role role;

  @Enumerated(EnumType.STRING)
  private Status status;

  private String frequency;

  @Column(name = "last_visited_flat_number")
  private String lastVisitedFlatNumber;


}

