package com.cstapin.auth.domain;

import com.cstapin.support.domain.AbstractEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "token")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
public class Token extends AbstractEntity {

    @Column(name = "access_token", nullable = false)
    private String accessToken;

    @Column(name = "refresh_token", nullable = false, unique = true)
    private String refreshToken;

    public Token(String accessToken, String refreshToken, LocalDateTime createdAt, LocalDateTime updatedAt) {

        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        super.createdAt = createdAt;
        super.updatedAt = updatedAt;
    }

    public Token(String accessToken, String refreshToken) {
        this(accessToken, refreshToken, LocalDateTime.now(), LocalDateTime.now());
    }

    public boolean isExpired(LocalDateTime now) {
        return updatedAt.plusDays(30).isBefore(now);
    }

    public void update(JwtReissueValidator validator, String accessToken, String refreshToken, LocalDateTime time) {
        validator.validate(this, time);
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

}
