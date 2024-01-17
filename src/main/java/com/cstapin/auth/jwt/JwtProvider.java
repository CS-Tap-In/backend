package com.cstapin.auth.jwt;

import com.cstapin.auth.jwt.properties.JwtProperties;
import com.cstapin.member.domain.Credentials;
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

    public String createAccessToken(Credentials credentials) {
        return createToken(
                credentials.getUsername(),
                credentials.getRole().name(),
                jwtProperties.getAccessTokenExpirationPeriod(),
                jwtProperties.getAccessTokenSecretKey()
        );
    }

    public String createRefreshToken() {
        return UUID.randomUUID().toString();
    }

    public String getUsernameFromAccessToken(String accessToken) {
        Claims claims = getClaims(accessToken, jwtProperties.getAccessTokenSecretKey());
        return claims.getSubject();
    }

    public String getRoleFromAccessToken(String accessToken) {
        Claims claims = getClaims(accessToken, jwtProperties.getAccessTokenSecretKey());
        return claims.get("role", String.class);
    }

    private Claims getClaims(String token, String secretKey) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    private String createToken(String username, String role, Long expiredPeriod, String secretKey) {
        Claims claims = Jwts.claims().setSubject(username);
        Date now = new Date();
        Date validity = new Date(now.getTime() + expiredPeriod);

        return Jwts.builder()
                .setClaims(claims)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

}
