package com.cstapin.member.domain;

import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
public class Credentials {
    private final String username;

    private final String password;

    private final Long tokenId;
    private final MemberRole role;

    public Credentials(String username, String password, Long tokenId, MemberRole role) {
        this.username = username;
        this.password = password;
        this.tokenId = tokenId;
        this.role = role;
    }

    public Credentials(String username, String password, MemberRole role) {
        this(username, password, null, role);
    }

    public Credentials withdraw() {
        return new Credentials("withdrawal_" + this.username, password, tokenId, role);
    }

    public boolean matchPassword(PasswordEncoder passwordEncoder, String password) {
        return passwordEncoder.matches(password, this.password);
    }

    public Credentials updateToken(Long tokenId) {
        return new Credentials(username, password, tokenId, role);
    }
}
