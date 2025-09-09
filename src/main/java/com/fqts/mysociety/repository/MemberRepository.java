package com.fqts.mysociety.repository;

import com.fqts.mysociety.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MemberRepository extends JpaRepository<Member, UUID> {

    @Query(value = "SELECT * from member where mobile= :mobile", nativeQuery = true)
    Optional<Member> findByMobile(@Param("mobile") String mobile);

    @Query("SELECT m FROM Member m WHERE m.activationToken = :token")
    Optional<Member> findByActivationToken(@Param("token") String token);

    boolean existsByMobile(String mobile);
}
