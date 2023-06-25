package com.cstapin.auth.service;

import com.cstapin.auth.domain.PrincipalDetails;
import com.cstapin.auth.jwt.JwtProvider;
import com.cstapin.member.domain.Member;
import com.cstapin.member.domain.MemberRepository;
import com.cstapin.member.dto.MemberRequest;
import com.cstapin.member.dto.MemberResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new AccessDeniedException("권한이 없습니다."));

        return new PrincipalDetails(member);
    }

    @Transactional
    public MemberResponse.LoginResponse login(MemberRequest.LoginRequest request) {
        Member member = memberRepository.findByUsername(request.getUsername())
                .filter(m -> m.matchPassword(passwordEncoder, request.getPassword()))
                .orElseThrow(() -> new AccessDeniedException("권한이 없습니다."));

        return new MemberResponse.LoginResponse(jwtProvider.createAccessToken(member), jwtProvider.createRefreshToken(member));
    }
}
