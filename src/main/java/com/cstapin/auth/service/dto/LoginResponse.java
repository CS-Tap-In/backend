package com.cstapin.auth.service.dto;

import com.cstapin.auth.domain.Token;
import com.cstapin.member.domain.Member;
import lombok.Getter;

@Getter
public class LoginResponse {
    private final String accessToken;
    private final String refreshToken;
    private final Member.MemberRole role;

    public LoginResponse(String accessToken, String refreshToken, Member.MemberRole role) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.role = role;
    }

    public LoginResponse(Token token, Member.MemberRole role) {
        this(token.getAccessToken(), token.getRefreshToken(), role);
    }

}
