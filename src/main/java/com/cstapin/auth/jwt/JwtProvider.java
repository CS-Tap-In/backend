package com.cstapin.auth.jwt;

import com.cstapin.auth.properties.JwtProperties;
import com.cstapin.member.domain.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final JwtProperties jwtProperties;

    public String createAccessToken(Member member) {
        return createToken(member, jwtProperties.getAccessTokenExpirationPeriod(), jwtProperties.getAccessTokenSecretKey());
    }

    public String createRefreshToken() {
        return UUID.randomUUID().toString();
    }

    private String createToken(Member member, Long expiredPeriod, String secretKey) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + expiredPeriod);

        return Jwts.builder()
                .setClaims(createClaims(member))
                .setSubject(member.getUsername())
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    private Claims createClaims(Member member) {
        Claims claims = Jwts.claims();
        claims.put("id", member.getId());
        return claims;
    }

}
