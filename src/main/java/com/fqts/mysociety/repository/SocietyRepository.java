package com.fqts.mysociety.repository;

import com.fqts.mysociety.model.Society;
import com.fqts.mysociety.model.Status;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SocietyRepository extends JpaRepository<Society, UUID> {

  Optional<Society> findById(UUID societyId);

  @Query(value = "SELECT society_unique_code FROM society ORDER BY CAST(regexp_replace(society_unique_code, '[^0-9]', '', 'g') AS INTEGER) DESC LIMIT 1", nativeQuery = true)
  String findLastSocietyCode();

  List<Society> findByStatusAndRegisteredAtBefore(Status status, Date registeredBefore);

  @Query(
          value = "SELECT * FROM society  " +
                  "WHERE status IN ('APPROVED','AUTOAPPROVED') " +
                  "ORDER BY registered_at DESC",
          nativeQuery = true
  )
  List<Society> findApprovedOrAutoApprovedSocieties();
}
