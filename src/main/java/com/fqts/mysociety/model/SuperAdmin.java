package com.fqts.mysociety.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "super_admin")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SuperAdmin {
    @Id
    @Column(name = "member_id", updatable = false, nullable = false)
    private UUID superAdminId = UUID.randomUUID();

    private String mobile;
    private String email;
    private String fullName;
    private String password;
    private String role;
}
