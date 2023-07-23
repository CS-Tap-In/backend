package com.cstapin.auth.jwt;

import com.cstapin.auth.jwt.properties.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtProperties jwtProperties;

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

    public void validateAccessToken(String accessToken) {
        Claims claims = getClaims(accessToken, jwtProperties.getAccessTokenSecretKey());
        if (claims.getExpiration().before(new Date())) {
            throw new IllegalStateException("토큰 만료");
        }
    }

    public boolean isExpiredToken(String accessToken) {
        try {
            Jwts.parser().setSigningKey(jwtProperties.getAccessTokenSecretKey()).parse(accessToken);
        } catch (ExpiredJwtException e) {
            return true;
        }
        return false;
    }
}
