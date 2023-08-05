package com.cstapin.auth.service;

import com.cstapin.auth.domain.*;
import com.cstapin.auth.jwt.JwtProvider;
import com.cstapin.auth.service.dto.*;
import com.cstapin.member.domain.Member;
import com.cstapin.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final JoinValidator joinValidator;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final TokenRepository tokenRepository;
    private final JwtReissueValidator jwtReissueValidator;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new AccessDeniedException("권한이 없습니다."));

        return new PrincipalDetails(member);
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {
        Member member = memberRepository.findByUsername(request.getUsername())
                .filter(m -> m.matchPassword(passwordEncoder, request.getPassword()))
                .orElseThrow(() -> new IllegalArgumentException("잘못된 아이디와 비밀번호 입니다."));

        Token token = tokenRepository.save(
                new Token(jwtProvider.createAccessToken(member), jwtProvider.createRefreshToken())
        );
        member.updateToken(token.getId());

        return new LoginResponse(token);
    }

    @Transactional
    public void join(JoinAdminRequest request, Member.MemberRole memberRole) {
        joinValidator.validate(request, memberRole);
        memberRepository.save(
                Member.builder().username(request.getUsername()).nickname(request.getNickname())
                        .password(passwordEncoder.encode(request.getPassword())).role(memberRole).build()
        );
    }

    @Transactional
    public TokenResponse reissueToken(ReissueTokenRequest request) {

        Token token = tokenRepository.findByRefreshToken(request.getRefreshToken())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 refreshToken 입니다."));

        Member member = memberRepository.findByTokenId(token.getId())
                .orElseThrow(() -> new IllegalStateException("만료된 refresh token 입니다."));

        token.update(
                jwtReissueValidator,
                jwtProvider.createAccessToken(member),
                jwtProvider.createRefreshToken(),
                LocalDateTime.now()
        );

        return new TokenResponse(token);
    }
}
