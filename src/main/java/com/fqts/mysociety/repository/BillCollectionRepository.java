package com.fqts.mysociety.repository;

import com.fqts.mysociety.dto.response.BillCollectionResponseDTO;
import com.fqts.mysociety.model.BillCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BillCollectionRepository extends JpaRepository<BillCollection, UUID> {


    @Query("SELECT new com.fqts.mysociety.dto.response.BillCollectionResponseDTO(" +
            "bc.collectionId, bc.matrixCharges.matrixChargesId, bc.flat.id.flatNumber, bc.paidAmount, bc.paidDate, " +
            "bc.paymentMode, bc.transactionRefNumber, bc.isVerified, bc.verificationDate, bc.verifiedBy, bc.society.societyId) " +
            "FROM BillCollection bc WHERE bc.isVerified = false")
    List<BillCollectionResponseDTO> findByIsVerifiedFalse();

    List<BillCollection> findByIsVerifiedFalseAndImageIsNull();

    @Query("SELECT b FROM BillCollection b " +
            "JOIN FETCH b.flat f " +
            "JOIN FETCH b.matrixCharges " +
            "JOIN FETCH b.society " +
            "WHERE b.society.societyId = :societyId")
    List<BillCollection> findBySocietySocietyId(@Param("societyId") UUID societyId);

    @Query("SELECT b FROM BillCollection b " +
            "JOIN FETCH b.flat f " +
            "JOIN FETCH b.matrixCharges " +
            "JOIN FETCH b.society " +
            "WHERE f.id.flatNumber = :flatNumber")
    List<BillCollection> findByFlat_Id_FlatNumber(@Param("flatNumber") String flatNumber);
}