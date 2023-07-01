package com.cstapin.auth.service;

import com.cstapin.auth.domain.PrincipalDetails;
import com.cstapin.auth.domain.Token;
import com.cstapin.auth.domain.TokenRepository;
import com.cstapin.auth.jwt.JwtProvider;
import com.cstapin.auth.jwt.JwtUtil;
import com.cstapin.member.domain.Member;
import com.cstapin.member.domain.MemberRepository;
import com.cstapin.member.dto.MemberResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.cstapin.member.dto.MemberRequest.*;
import static com.cstapin.member.dto.MemberResponse.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenMapper tokenMapper;
    private final TokenRepository tokenRepository;
    private final JwtUtil jwtUtil;
    private final JwtProvider jwtProvider;

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

        Token token = tokenMapper.mapFrom(member);

        tokenRepository.findByMemberId(member.getId())
                .ifPresentOrElse(
                        t -> t.updateToken(token.getAccessToken(), token.getRefreshToken()),
                        () -> tokenRepository.save(token)
                );

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
        // 0. refreshToken 을 가지는 Token entity 를 db 에서 가져온다.
        Token token = tokenRepository.findByRefreshToken(request.getRefreshToken())
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 refreshToken 입니다."));

        // 1. accessToken 이 만료된 토큰이 아니면 정상적이지 않은 호출로 간주하고 예외처리한다.
        if (!jwtUtil.isExpiredToken(token.getAccessToken())) {
            log.warn("accessToken 이 만료되지 않았음에도 token 재발급 api 호출. userId = {}", token.getMemberId());
            throw new IllegalStateException("비정상적인 호출입니다.");
        }

        // 3. modifiedAt이 30일이 넘지 않았는지 확인한다.
        if (token.isExpired(LocalDateTime.now())) {
            throw new IllegalStateException("로그인 한지 30일이 지났습니다. 다시 로그인 해주세요.");
        }

        Member member = memberRepository.findById(token.getMemberId())
                .orElseThrow(EntityNotFoundException::new);

        // 4. 새로운 accessToken 과 refreshToken 을 발급하고 Token 을 update 친다.
        token.updateToken(jwtProvider.createAccessToken(member), UUID.randomUUID().toString());

        // 5. TokenResponse 에 accessToken 과 refreshToken 을 담아서 반환한다.
        return new TokenResponse(token);
    }
}
