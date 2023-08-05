package com.cstapin.auth.service.dto;

import com.cstapin.auth.domain.Token;
import lombok.Getter;

@Getter
public class LoginResponse {
    private final String accessToken;
    private final String refreshToken;

    public LoginResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public LoginResponse(Token token) {
        this(token.getAccessToken(), token.getRefreshToken());
    }

}
