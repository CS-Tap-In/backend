package com.cstapin.member.service.dto;

import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class DailyGoalRequest {

    @NotNull
    private int dailyGoal;
}
