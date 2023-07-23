package com.cstapin.auth.service.dto;

import com.cstapin.auth.domain.Token;
import lombok.Getter;

public class MemberResponse {

    @Getter
    public static class LoginResponse {
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

    @Getter
    public static class TokenResponse {
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
}
