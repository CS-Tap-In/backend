package com.cstapin.member.service.dto;

import com.cstapin.member.domain.Member;
import com.cstapin.member.domain.Profiles;
import com.cstapin.member.persistence.MemberEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProfileResponse {
    private final Long memberId;
    private final String nickname;
    private final String avatarUrl;
    private final int completeQuizCount;
    private final int dailyGoal;
    private final boolean isDailyLearningComplete;

    @Builder
    public ProfileResponse(Long memberId,
                           String nickname,
                           String avatarUrl,
                           int completeQuizCount,
                           int dailyGoal,
                           boolean isDailyLearningComplete) {
        this.memberId = memberId;
        this.nickname = nickname;
        this.avatarUrl = avatarUrl;
        this.completeQuizCount = completeQuizCount;
        this.dailyGoal = dailyGoal;
        this.isDailyLearningComplete = isDailyLearningComplete;
    }

    public static ProfileResponse of(Long memberId,
                                     Profiles profiles,
                                     int completeQuizCount,
                                     boolean isDailyLearningComplete) {
        return ProfileResponse.builder()
                .memberId(memberId)
                .nickname(profiles.getNickname())
                .avatarUrl(profiles.getAvatarUrl())
                .completeQuizCount(completeQuizCount)
                .dailyGoal(profiles.getDailyGoal())
                .isDailyLearningComplete(isDailyLearningComplete)
                .build();
    }
}
