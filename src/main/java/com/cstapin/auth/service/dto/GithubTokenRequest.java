package com.cstapin.auth.service.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class GithubTokenRequest {

    @NotBlank
    private String code;
}
