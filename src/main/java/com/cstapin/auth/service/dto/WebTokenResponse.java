package com.cstapin.auth.service.dto;

import lombok.Getter;

@Getter
public class WebTokenResponse {

    private final String webToken;

    public WebTokenResponse(String webToken) {
        this.webToken = webToken;
    }
}
