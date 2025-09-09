package com.fqts.mysociety.repository;


import com.fqts.mysociety.model.MaintenanceCharges;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MaintenanceChargesRepository extends JpaRepository<MaintenanceCharges, UUID> {
}
