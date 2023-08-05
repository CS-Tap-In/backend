package com.cstapin.auth.service.dto;

import lombok.Getter;

import javax.validation.constraints.NotEmpty;

@Getter
public class ReissueTokenRequest {
    @NotEmpty
    private String refreshToken;
}

