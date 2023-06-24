package com.cstapin.member.dto;

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
    }
}
