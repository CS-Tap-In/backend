package com.cstapin.auth.domain;

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
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "access_token", nullable = false)
    private String accessToken;

    @Column(name = "refresh_token", nullable = false, unique = true)
    private String refreshToken;

    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "modified_at", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime modifiedAt;

    @PreUpdate
    public void preUpdate() {
        this.modifiedAt = LocalDateTime.now();
    }

    public Token(String accessToken, String refreshToken, LocalDateTime createdAt, LocalDateTime modifiedAt) {

        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public Token(String accessToken, String refreshToken) {
        this(accessToken, refreshToken, LocalDateTime.now(), LocalDateTime.now());
    }

    public boolean isExpired(LocalDateTime now) {
        return this.modifiedAt.plusDays(30).isBefore(now);
    }

    public void update(JwtReissueValidator validator, String accessToken, String refreshToken, LocalDateTime time) {
        validator.validate(this, time);
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

}
