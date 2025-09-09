package com.fqts.mysociety.repository;

import com.fqts.mysociety.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface StaffRepository extends JpaRepository<Staff, UUID> {

    @Query(value = "SELECT * FROM staff WHERE society_id = :societyId", nativeQuery = true)
    List<Staff> getStaffBySocietyId(@Param("societyId") UUID societyId);

}
