package com.fqts.mysociety.repository;

import com.fqts.mysociety.model.SuperAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SuperAdminRepository extends JpaRepository<SuperAdmin, UUID> {
    @Query(value = "SELECT * from super_admin where mobile= :mobile", nativeQuery = true)
    Optional<SuperAdmin> findByMobile(@Param("mobile") String mobile);
}
