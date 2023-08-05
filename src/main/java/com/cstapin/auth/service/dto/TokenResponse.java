package com.cstapin.auth.service.dto;

import com.cstapin.auth.domain.Token;
import lombok.Getter;

@Getter
public class TokenResponse {
    private final String accessToken;
    private final String refreshToken;

    public TokenResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public TokenResponse(Token token) {
        this(token.getAccessToken(), token.getRefreshToken());
    }

}
