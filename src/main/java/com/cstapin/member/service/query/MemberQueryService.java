package com.cstapin.member.service.query;

import com.cstapin.exception.notfound.MemberNotFoundException;
import com.cstapin.member.domain.Member;
import com.cstapin.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberQueryService {

    private final MemberRepository memberRepository;

    public Member findByTokenId(Long tokenId) {
        return memberRepository.findByTokenId(tokenId).orElseThrow(() -> new MemberNotFoundException("만료된 token 입니다."));
    }

    public Member findByUsername(String username) {
        return memberRepository.findByUsername(username).orElseThrow(MemberNotFoundException::new);
    }
}
