package com.cstapin.auth.oauth2.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class GithubProfileResponse {
    private Long id;
    private String name;
    @JsonProperty(value = "avatar_url")
    private String avatarUrl;

    public GithubProfileResponse() {
    }

}
