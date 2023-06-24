package com.cstapin.member.service;

import com.cstapin.auth.jwt.JwtProvider;
import com.cstapin.member.domain.Member;
import com.cstapin.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.cstapin.member.dto.MemberRequest.LoginRequest;
import static com.cstapin.member.dto.MemberResponse.LoginResponse;

@Slf4j
@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Transactional
    public LoginResponse login(LoginRequest request) {
        Member member = memberRepository.findByUsername(request.getUsername())
                .filter(m -> m.matchPassword(passwordEncoder, request.getPassword()))
                .orElseThrow(() -> new AccessDeniedException("권한이 없습니다."));

        return new LoginResponse(jwtProvider.createAccessToken(member), jwtProvider.createRefreshToken(member));
    }
}
