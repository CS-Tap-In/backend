package com.cstapin.member.domain;

import lombok.Getter;

@Getter
public class Profiles {
    private static final int START_DAILY_GOAL = 10;
    private final String nickname;

    private final String avatarUrl;

    private final int dailyGoal;

    public Profiles(String nickname, String avatarUrl, int dailyGoal) {
        this.nickname = nickname;
        this.avatarUrl = avatarUrl;
        this.dailyGoal = dailyGoal;
    }

    public Profiles(String nickname, String avatarUrl) {
        this(nickname, avatarUrl, START_DAILY_GOAL);
    }

    public Profiles(String nickname) {
        this(nickname, null);
    }

    public Profiles changeDailyGoal(int dailyGoal) {
        return new Profiles(nickname, avatarUrl, dailyGoal);
    }
}
