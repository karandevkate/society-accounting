package com.fqts.mysociety.repository;

import com.fqts.mysociety.model.SocietyExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SocietyExpenseRepository extends JpaRepository<SocietyExpense, UUID> {

    @Query(value = "SELECT * FROM society_expense WHERE society_id = :societyId", nativeQuery = true)
    List<SocietyExpense> findSocietyExpenseBySocietyID(@Param("societyId") UUID societyId);
}
