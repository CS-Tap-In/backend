package com.cstapin.member.persistence;

import com.cstapin.exception.notfound.MemberNotFoundException;
import com.cstapin.member.domain.Member;
import com.cstapin.member.service.MemberRepository;
import com.cstapin.member.service.dto.MembersRequest;
import com.cstapin.member.service.dto.MembersResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {

    private final MemberJpaRepository memberJpaRepository;

    @Override
    public Member getByTokenId(Long tokenId) {
        return memberJpaRepository.findByTokenId(tokenId)
                .map(Member::from)
                .orElseThrow(() -> new MemberNotFoundException("만료된 token 입니다."));
    }

    @Override
    public Optional<Member> findByUsername(String username) {
        return memberJpaRepository.findByUsername(username).map(Member::from);
    }

    @Override
    public Member getByUsername(String username) {
        return memberJpaRepository.findByUsername(username)
                .map(Member::from)
                .orElseThrow(MemberNotFoundException::new);
    }

    public Page<MembersResponse> findMembers(MembersRequest request) {
        return memberJpaRepository.findMembers(request).map(MembersResponse::from);
    }

    @Override
    public Member save(Member member) {
        return Member.from(memberJpaRepository.save(member.toMemberEntity()));
    }

}


