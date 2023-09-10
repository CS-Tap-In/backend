package com.cstapin.member.domain;

import com.cstapin.support.domain.AbstractEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends AbstractEntity {

    public enum MemberRole {USER, ADMIN}

    @Column(name = "username", length = 50, nullable = false, unique = true)
    private String username;

    @Column(name = "password", length = 100, nullable = false)
    private String password;

    @Column(name = "nickname", length = 30, nullable = false)
    private String nickname;

    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 20, nullable = false)
    private MemberRole role;

    @Column(name = "token_id", unique = true)
    private Long tokenId;

    public boolean matchPassword(PasswordEncoder passwordEncoder, String password) {
        return passwordEncoder.matches(password, this.password);
    }

    @Builder
    public Member(String username, String password, String nickname, MemberRole role) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
    }

    public void updateToken(Long tokenId) {
        this.tokenId = tokenId;
    }

    public void updateAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
