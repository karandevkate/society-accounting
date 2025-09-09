package com.fqts.mysociety.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "member")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    @Id
    @Column(name = "member_id", updatable = false, nullable = false)
    private UUID memberId = UUID.randomUUID();

    @Column(nullable = false, unique = true)
    private String mobile;
    private String fullName;
    private String alternateMobile;
    private String email;
    private String alternateEmail;
    private String nativeAddress;
    private String nativeMobile;
    private String password;

    private String govt_id_proof_type;

    private String govId;

    private String activationToken;
    private boolean activated;

    private Date createdAt = new Date();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<SocietyMember> societyMembers;


    @Column(name = "last_visited_society_id")
    private UUID lastVisitedSocietyId;

}
