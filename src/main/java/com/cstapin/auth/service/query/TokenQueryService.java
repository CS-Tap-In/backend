package com.cstapin.auth.service.query;

import com.cstapin.auth.domain.Token;
import com.cstapin.auth.domain.TokenRepository;
import com.cstapin.exception.notfound.TokenNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenQueryService {

    private final TokenRepository tokenRepository;

    public Token findByRefreshToken(String refreshToken) {
        return tokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new TokenNotFoundException("존재하지 않는 refreshToken 입니다."));
    }
}
