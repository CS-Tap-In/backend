package com.cstapin.auth.service.dto;

import com.cstapin.auth.domain.Token;
import com.cstapin.member.domain.MemberRole;
import com.cstapin.member.persistence.MemberEntity;
import lombok.Getter;

@Getter
public class LoginResponse {
    private final String accessToken;
    private final String refreshToken;
    private final MemberRole role;

    public LoginResponse(String accessToken, String refreshToken, MemberRole role) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.role = role;
    }

    public LoginResponse(Token token, MemberRole role) {
        this(token.getAccessToken(), token.getRefreshToken(), role);
    }

}
