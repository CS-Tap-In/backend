package com.cstapin.member.persistence;

import com.cstapin.member.domain.MemberRole;
import com.cstapin.support.domain.AbstractEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "member")
@Getter
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberEntity extends AbstractEntity {

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

    @Column(name = "daily_goal")
    private int dailyGoal = 10;

    @Column(name = "token_id", unique = true)
    private Long tokenId;

    @Builder
    public MemberEntity(Long id,
                        String username,
                        String password,
                        String nickname,
                        String avatarUrl,
                        MemberRole role,
                        int dailyGoal,
                        Long tokenId,
                        LocalDateTime createdAt,
                        LocalDateTime updatedAt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.avatarUrl = avatarUrl;
        this.role = role;
        this.dailyGoal = dailyGoal;
        this.tokenId = tokenId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public boolean matchPassword(PasswordEncoder passwordEncoder, String password) {
        return passwordEncoder.matches(password, this.password);
    }

    public void withdraw() {
        this.username = "withdrawal_" + this.username;
    }

    public void updateToken(Long tokenId) {
        this.tokenId = tokenId;
    }

}
