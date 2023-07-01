package com.cstapin.auth.domain;

import com.cstapin.auth.validator.JwtReissueValidator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId; // one to one 관계

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

    public Token(Long memberId,
                 String accessToken,
                 String refreshToken,
                 LocalDateTime createdAt,
                 LocalDateTime modifiedAt) {
        this.memberId = memberId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public Token(Long memberId, String accessToken, String refreshToken) {
        this(memberId, accessToken, refreshToken, LocalDateTime.now(), LocalDateTime.now());
    }

    public void updateToken(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public boolean isExpired(LocalDateTime now) {
        return this.modifiedAt.plusDays(30).isBefore(now);
    }

    public void updateToken(JwtReissueValidator validator, Token token, LocalDateTime time) {
        validator.validate(this, time);
        this.accessToken = token.getAccessToken();
        this.refreshToken = token.getRefreshToken();
    }

}
