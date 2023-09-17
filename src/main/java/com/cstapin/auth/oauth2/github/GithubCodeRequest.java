package com.cstapin.auth.oauth2.github;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class GithubCodeRequest {

    @NotBlank
    private String code;
}
