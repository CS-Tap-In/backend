package com.cstapin.quiz.service.dto;

import com.cstapin.quiz.domain.QuizParticipants;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

public class QuizParticipantsRequest {

    @Pattern(regexp = "^[ㄱ-ㅎ가-힣]*$")
    private String phoneNumber;

    @Pattern(regexp = "^[0-9]*$")
    private String username;

    @Min(1)
    private int correctCount;

    public QuizParticipants toQuizParticipants() {
        return QuizParticipants.builder()
                .phoneNumber(phoneNumber)
                .username(username)
                .correctCount(correctCount)
                .build();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
