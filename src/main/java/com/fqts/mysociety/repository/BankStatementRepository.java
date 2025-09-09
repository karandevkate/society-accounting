package com.fqts.mysociety.repository;

import com.fqts.mysociety.model.BankStatement;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BankStatementRepository extends JpaRepository<BankStatement, UUID> {

  // For credit transactions: match description containing transaction ref and credit amount
  @Query("SELECT bs FROM BankStatement bs WHERE bs.description LIKE CONCAT('%', :transactionRefNumber, '%') AND bs.creditAmount = :amount")
  Optional<BankStatement> findByDescriptionContainingAndCreditAmount(
      @Param("transactionRefNumber") String transactionRefNumber,
      @Param("amount") Double amount
  );

  // Used for record-level deduplication based on hash
  boolean existsByTransactionHash(String transactionHash);

  // Optional: For future upsert strategies, e.g., match on date + amount + description
  @Query("SELECT bs FROM BankStatement bs WHERE bs.transactionDate = :transactionDate AND bs.description = :description AND (bs.debitAmount = :debitAmount OR bs.creditAmount = :creditAmount)")
  Optional<BankStatement> findByTransactionDetails(
      @Param("transactionDate") String transactionDate,
      @Param("description") String description,
      @Param("debitAmount") Double debitAmount,
      @Param("creditAmount") Double creditAmount
  );
}
