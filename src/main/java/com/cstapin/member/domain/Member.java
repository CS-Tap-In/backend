package com.cstapin.member.domain;

import com.cstapin.auth.oauth2.github.GithubProfileResponse;
import com.cstapin.member.persistence.MemberEntity;
import com.cstapin.quiz.domain.DailySelectedQuizzes;
import com.cstapin.quiz.service.DailyQuizSelector;
import com.cstapin.support.domain.AbstractDomain;
import lombok.Builder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

public class Member extends AbstractDomain {

    private final Credentials credentials;

    private final Profiles profiles;

    @Builder
    public Member(Long id,
                  LocalDateTime createdAt,
                  LocalDateTime updatedAt,
                  Credentials credentials,
                  Profiles profiles) {
        super(id, createdAt, updatedAt);
        this.credentials = credentials;
        this.profiles = profiles;
    }

    public static Member from(MemberEntity memberEntity) {
        return Member.builder()
                .id(memberEntity.getId())
                .credentials(new Credentials(memberEntity.getUsername(), memberEntity.getPassword(),
                        memberEntity.getTokenId(), memberEntity.getRole()))
                .profiles(new Profiles(memberEntity.getNickname(),
                        memberEntity.getAvatarUrl(), memberEntity.getDailyGoal()))
                .createdAt(memberEntity.getCreatedAt())
                .updatedAt(memberEntity.getUpdatedAt())
                .build();
    }

    public static Member githubMember(String githubUsernamePrefix, GithubProfileResponse githubProfile) {
        return Member.builder()
                .credentials(new Credentials(githubUsernamePrefix + githubProfile.getId(), "",
                        null, MemberRole.USER))
                .profiles(new Profiles(githubProfile.getName(), githubProfile.getAvatarUrl()))
                .build();
    }

    public MemberEntity toMemberEntity() {
        return MemberEntity.builder()
                .id(id)
                .username(credentials.getUsername())
                .password(credentials.getPassword())
                .nickname(profiles.getNickname())
                .avatarUrl(profiles.getAvatarUrl())
                .dailyGoal(profiles.getDailyGoal())
                .tokenId(credentials.getTokenId())
                .role(credentials.getRole())
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }

    public Member changeDailyGoal(int dailyGoal) {
        return Member.builder()
                .id(id)
                .credentials(credentials)
                .profiles(profiles.changeDailyGoal(dailyGoal))
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }

    public Member withdraw() {
        return Member.builder()
                .id(id)
                .credentials(credentials.withdraw())
                .profiles(profiles)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }

    public DailySelectedQuizzes selectQuizzes(DailyQuizSelector dailyQuizSelector) {
        return dailyQuizSelector.select(id, profiles.getDailyGoal());
    }

    public Profiles getProfiles() {
        return profiles;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void validatePassword(PasswordEncoder passwordEncoder, String password) {
        if (!credentials.matchPassword(passwordEncoder, password)) {
            throw new IllegalArgumentException("잘못된 비밀번호 입니다.");
        }
    }

    public Member updateToken(Long tokenId) {
        return Member.builder()
                .id(id)
                .credentials(credentials.updateToken(tokenId))
                .profiles(profiles)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }

    public MemberRole getRole() {
        return credentials.getRole();
    }
}
