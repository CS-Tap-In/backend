package com.cstapin.member.domain;

import com.cstapin.member.dto.MemberRequest;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

import static com.cstapin.member.dto.MemberRequest.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    public enum MemberRole {USER, ADMIN}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(name = "username", length = 50, nullable = false)
    private String username;

    @Column(name = "password", length = 100, nullable = false)
    private String password;

    @Column(name = "nickname", length = 30, nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", length = 20, nullable = false)
    private MemberRole role;

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
}
