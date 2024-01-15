package com.cstapin.auth.domain;

import com.cstapin.auth.jwt.properties.JwtProperties;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtReissueValidator {

    private final JwtProperties jwtProperties;

    public void validate(Token token, LocalDateTime time) {

        // accessToken 이 만료된 토큰이 아니면 정상적이지 않은 호출로 간주하고 예외처리한다.
//        if (!isExpiredToken(token.getAccessToken())) {
//            log.warn("accessToken 이 만료되지 않았음에도 token 재발급 api 호출");
//            throw new IllegalStateException("비정상적인 호출입니다.");
//        }

        // updatedAt이 30일이 넘지 않았는지 확인한다.
        if (token.isExpired(time)) {
            throw new IllegalStateException("로그인 한지 30일이 지났습니다. 다시 로그인 해주세요.");
        }
    }

    private boolean isExpiredToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtProperties.getAccessTokenSecretKey()).parse(token);
        } catch (ExpiredJwtException e) {
            return true;
        }
        return false;
    }
}
