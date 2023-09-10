package com.cstapin.auth.oauth2.github;

import lombok.Getter;

@Getter
public class GithubAccessTokenRequest {

    private String code;
    private String client_id;
    private String client_secret;

    public GithubAccessTokenRequest(String code, String client_id, String client_secret) {
        this.code = code;
        this.client_id = client_id;
        this.client_secret = client_secret;
    }

    public GithubAccessTokenRequest() {
    }
}
