package com.fqts.mysociety.repository;

import com.fqts.mysociety.model.StaffSalary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StaffSalaryRepository extends JpaRepository<StaffSalary, UUID> {
    @Query(value = "SELECT * FROM staff_salary WHERE society_id = :societyId", nativeQuery = true)
    List<StaffSalary> findAllBySocietyID(@Param("societyId") UUID societyId);


    @Query("SELECT s.society, SUM(s.amount) FROM StaffSalary s WHERE MONTH(s.paymentDate) = :month AND YEAR(s.paymentDate) = :year GROUP BY s.society")
    List<Object[]> findTotalPaidAndSocietyByMonthAndYear(@Param("month") int month, @Param("year") int year);



    @Query(value = "SELECT * FROM staff_salary s WHERE EXTRACT(MONTH FROM s.payment_date) = :month AND EXTRACT(YEAR FROM s.payment_date) = :year ", nativeQuery = true)
    List<StaffSalary> findStaffSalaryByMonthAndYear(@Param("month") int month, @Param("year") int year);



}
