package com.cstapin.member.service.dto;

import com.cstapin.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProfileResponse {
    private final Long memberId;
    private final String nickname;
    private final String avatarUrl;
    private final int completeQuizCount;
    private final int dailyGoal;

    @Builder
    public ProfileResponse(Long memberId, String nickname, String avatarUrl, int completeQuizCount, int dailyGoal) {
        this.memberId = memberId;
        this.nickname = nickname;
        this.avatarUrl = avatarUrl;
        this.completeQuizCount = completeQuizCount;
        this.dailyGoal = dailyGoal;
    }

    public static ProfileResponse of(Member member, int completeQuizCount) {
        return ProfileResponse.builder()
                .memberId(member.getId())
                .nickname(member.getNickname())
                .avatarUrl(member.getAvatarUrl())
                .completeQuizCount(completeQuizCount)
                .dailyGoal(member.getDailyGoal())
                .build();
    }
}
