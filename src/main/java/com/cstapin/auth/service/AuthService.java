package com.cstapin.auth.service;

import com.cstapin.auth.domain.PrincipalDetails;
import com.cstapin.auth.domain.Token;
import com.cstapin.auth.domain.TokenRepository;
import com.cstapin.auth.validator.JwtReissueValidator;
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

import static com.cstapin.member.dto.MemberRequest.*;
import static com.cstapin.member.dto.MemberResponse.LoginResponse;
import static com.cstapin.member.dto.MemberResponse.TokenResponse;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenMapper tokenMapper;
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
                .orElseThrow(() -> new AccessDeniedException("권한이 없습니다."));

        Token token = tokenRepository.save(tokenMapper.mapFrom(member));
        member.updateToken(token.getId());

        return new LoginResponse(token);
    }

    @Transactional
    public void join(JoinRequest request, Member.MemberRole memberRole) {
        if (memberRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("다른 아이디를 사용해주세요.");
        }

        memberRepository.save(
                Member.builder().username(request.getUsername()).nickname(request.getNickname())
                        .password(passwordEncoder.encode(request.getPassword())).role(memberRole).build()
        );
    }

    @Transactional
    public TokenResponse reissueToken(ReissueTokenRequest request) {

        Token token = tokenRepository.findByRefreshToken(request.getRefreshToken())
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 refreshToken 입니다."));

        Member member = memberRepository.findByTokenId(token.getId())
                .orElseThrow(() -> new IllegalStateException("만료된 token 입니다."));

        Token newToken = tokenMapper.mapFrom(member);

        token.updateToken(jwtReissueValidator, newToken, LocalDateTime.now());

        return new TokenResponse(token);
    }
}
