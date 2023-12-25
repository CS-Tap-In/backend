package com.cstapin.member.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<MemberEntity, Long>, MemberRepositoryCustom {

    Optional<MemberEntity> findByUsername(String username);

    @Query("select m from MemberEntity m where m.tokenId = :tokenId")
    Optional<MemberEntity> findByTokenId(@Param(value = "tokenId") Long id);
}
