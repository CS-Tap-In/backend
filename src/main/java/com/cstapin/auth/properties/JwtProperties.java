package com.cstapin.auth.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "props.jwt")
public class JwtProperties {

    private String refreshTokenSecretKey;
    private String accessTokenSecretKey;
    private Long accessTokenExpirationPeriod;
    private Long refreshTokenExpirationPeriod;
}
