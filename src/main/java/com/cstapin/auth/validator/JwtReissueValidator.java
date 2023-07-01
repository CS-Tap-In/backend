package com.cstapin.auth.validator;

import com.cstapin.auth.domain.Token;
import com.cstapin.auth.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtReissueValidator {

    private final JwtUtil jwtUtil;

    public void validate(Token token, LocalDateTime time) {

        // accessToken 이 만료된 토큰이 아니면 정상적이지 않은 호출로 간주하고 예외처리한다.
        if (!jwtUtil.isExpiredToken(token.getAccessToken())) {
            log.warn("accessToken 이 만료되지 않았음에도 token 재발급 api 호출. userId = {}", token.getMemberId());
            throw new IllegalStateException("비정상적인 호출입니다.");
        }

        // modifiedAt이 30일이 넘지 않았는지 확인한다.
        if (token.isExpired(time)) {
            throw new IllegalStateException("로그인 한지 30일이 지났습니다. 다시 로그인 해주세요.");
        }
    }
}
