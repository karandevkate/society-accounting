package com.fqts.mysociety.repository;

import com.fqts.mysociety.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SocietyMemberRepository extends JpaRepository<SocietyMember, UUID> {

  @Query(value = "SELECT * FROM society_member WHERE society_id = :societyId AND role = 'ADMIN'", nativeQuery = true)
  List<SocietyMember> findAdminsBySocietyId(@Param("societyId") UUID societyId);

  @Query(value = "SELECT * FROM society_member WHERE society_id = :societyId", nativeQuery = true)
  List<SocietyMember> findMembersBySocietyId(@Param("societyId") UUID societyId);

  Optional<SocietyMember> findByMember(Member member);


  Optional<SocietyMember> findByMemberAndSociety(Member member, Society society);

  List<SocietyMember> findByStatus(Status status);

  List<SocietyMember> findBySocietySocietyId(UUID societyId);

  @Query(value = "SELECT * FROM society_member WHERE member_id = :memberId AND status IN ('AUTOAPPROVED', 'APPROVED')", nativeQuery = true)
  List<SocietyMember> findByMemberMemberId(@Param("memberId") UUID memberId);

  List<SocietyMember> findByMemberMemberIdAndSocietySocietyId(UUID memberId, UUID societyId);

  List<SocietyMember> findBySociety_SocietyId(UUID societyId);

  @Query(
      value = "SELECT m.email FROM society_member sm " +
          "JOIN member m ON sm.member_id = m.member_id " +
          "WHERE sm.flat_number = :flatNumber AND sm.society_id = :societyId",
      nativeQuery = true
  )
  String findEmailByFlatAndSociety(@Param("flatNumber") String flatNumber, @Param("societyId") UUID societyId);


  @Query(value = "SELECT * FROM society_member WHERE society_id = :societyId ", nativeQuery = true)
  List<SocietyMember> findBySocietyId(UUID societyId);

  @Query(value = "SELECT * FROM society_member WHERE society_id = :societyId ", nativeQuery = true)
  List<SocietyMember> findBySocietyIdANDRole(UUID societyId);

  @Query("SELECT sm.role FROM SocietyMember sm WHERE sm.member.memberId = :memberId")
  Role findRoleByMember_MemberId(UUID memberId);



  @Query("SELECT COUNT(sm) FROM SocietyMember sm WHERE sm.society.id = :societyId AND sm.status = 'PENDING'")
  long countPendingMembers(@Param("societyId") UUID societyId);

  @Query("SELECT COUNT(sm) FROM SocietyMember sm WHERE sm.society.id = :societyId AND sm.status = 'APPROVED'")
  long countApprovedMembers(@Param("societyId") UUID societyId);


  Optional<SocietyMember> findByMember_MemberIdAndSociety_SocietyId(UUID memberId,UUID societyID);

  List<SocietyMember> findAllByMember_MemberId(UUID memberId);

  @Query("SELECT sm FROM SocietyMember sm " +
          "WHERE sm.member.memberId = :memberId " +
          "AND sm.society.societyId = :societyId " +
          "AND sm.status IN ('APPROVED', 'AUTOAPPROVED')")
  Optional<SocietyMember> findApprovedMember(@Param("memberId") UUID memberId,
                                                   @Param("societyId") UUID societyId);

}
