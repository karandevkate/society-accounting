package com.fqts.mysociety.repository;

import com.fqts.mysociety.model.Journal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface JournalRepository extends JpaRepository<Journal, UUID> {


    @Query(value = """
    SELECT j.*
    FROM journal j
    JOIN flat f 
      ON j.flat_number = f.flat_number 
     AND j.society_id = f.society_id
    JOIN society_member sm 
      ON f.society_member_id = sm.society_member_id
    WHERE f.flat_number = :flatNumber
      AND j.transaction_date BETWEEN :startDate AND :endDate
    """, nativeQuery = true)
    List<Journal> findByFlatNumberAndTransactionDateBetween(
            @Param("flatNumber") String flatNumber,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );


    @Query(value = """
    SELECT j.*
    FROM journal j
    JOIN flat f 
      ON j.flat_number = f.flat_number
     AND j.society_id = f.society_id
    WHERE f.flat_number = :flatNumber
      AND f.society_id = :societyId
      AND j.transaction_date BETWEEN :startDate AND :endDate
    """, nativeQuery = true)
    List<Journal> findByFlatNumberAndSocietyIdAndTransactionDateBetween(
            @Param("flatNumber") String flatNumber,
            @Param("societyId") UUID societyId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );


    @Query(value = "SELECT * FROM journal WHERE society_id = :societyId", nativeQuery = true)
    List<Journal> findBySocietyId(@Param("societyId") UUID societyId);

    @Query(value = "SELECT * FROM journal WHERE society_id = :societyId AND flat_number = :flatNumber", nativeQuery = true)
    List<Journal> findBySocietyIdAndFlatNumber(@Param("societyId") UUID societyId, @Param("flatNumber") String flatNumber);
}
