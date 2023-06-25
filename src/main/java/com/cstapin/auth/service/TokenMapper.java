package com.cstapin.auth.service;

import com.cstapin.auth.domain.Token;
import com.cstapin.auth.jwt.JwtProvider;
import com.cstapin.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TokenMapper {

    private final JwtProvider jwtProvider;

    public Token mapFrom(Member member) {
        return new Token(
                member.getId(),
                jwtProvider.createAccessToken(member),
                UUID.randomUUID().toString()
        );
    }
}
