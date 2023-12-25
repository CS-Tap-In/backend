package com.cstapin.member.service;

import com.cstapin.member.domain.Member;
import com.cstapin.member.service.dto.MembersRequest;
import com.cstapin.member.service.dto.MembersResponse;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface MemberRepository {
    Member getByTokenId(Long tokenId);

    Optional<Member> findByUsername(String username);

    Member getByUsername(String username);

    Page<MembersResponse> findMembers(MembersRequest request);

    Member save(Member member);
}
