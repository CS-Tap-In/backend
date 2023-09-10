package com.cstapin.auth.oauth2.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class GithubAccessTokenResponse {

    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("token_type")
    private String tokenType;
    private String scope;
    private String bearer;

    public GithubAccessTokenResponse() {

    }

    public GithubAccessTokenResponse(String accessToken,
                                     String tokenType,
                                     String scope,
                                     String bearer) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.scope = scope;
        this.bearer = bearer;
    }
}
