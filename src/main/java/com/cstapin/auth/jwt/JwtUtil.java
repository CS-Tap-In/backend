package com.cstapin.auth.jwt;

import com.cstapin.auth.properties.JwtProperties;
import io.jsonwebtoken.Claims;
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

    private Claims getClaims(String token, String secretKey) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    public void validateAccessToken(String accessToken) {
        Claims claims = getClaims(accessToken, jwtProperties.getAccessTokenSecretKey());
        if (claims.getExpiration().before(new Date())) {
            throw new IllegalStateException("토큰 만료");
        }
    }
}
