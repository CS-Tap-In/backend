package com.cstapin.member.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(String username);

    @Query("select m from Member m where m.tokenId = :tokenId")
    Optional<Member> findByTokenId(@Param(value = "tokenId") Long id);
}
