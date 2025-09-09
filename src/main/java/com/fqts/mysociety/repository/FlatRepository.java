package com.fqts.mysociety.repository;

import com.fqts.mysociety.model.Flat;
import com.fqts.mysociety.model.FlatId;
import com.fqts.mysociety.model.Society;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FlatRepository extends JpaRepository<Flat, FlatId> {

  List<Flat> findBySociety(Society society);

  Optional<Flat> findById_FlatNumber(String flatNumber);


  Optional<Flat> findById_FlatNumberAndId_SocietyId(String flatNumber, UUID societyId);

  @Query(value = """
    SELECT f.* FROM flat f
    JOIN society_member sm ON f.society_member_id = sm.society_member_id
    WHERE f.society_member_id = :societyMemberId
      AND f.society_id = :societyId
      AND sm.status = 'APPROVED'
    """, nativeQuery = true)
  List<Flat> findBySocietyMemberIdAndSocietyId(@Param("societyMemberId") UUID societyMemberId,
                                               @Param("societyId") UUID societyId);



}
