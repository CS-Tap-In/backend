package com.cstapin.auth.service;

import com.cstapin.auth.domain.PrincipalDetails;
import com.cstapin.auth.domain.Token;
import com.cstapin.auth.domain.TokenRepository;
import com.cstapin.auth.jwt.JwtProvider;
import com.cstapin.member.domain.Member;
import com.cstapin.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.cstapin.member.dto.MemberRequest.JoinRequest;
import static com.cstapin.member.dto.MemberRequest.LoginRequest;
import static com.cstapin.member.dto.MemberResponse.LoginResponse;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenMapper tokenMapper;
    private final TokenRepository tokenRepository;

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
}
