package com.cstapin.quiz.service.dto;

import com.cstapin.quiz.domain.QuizParticipants;
import lombok.Getter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
public class QuizParticipantsRequest {

    @Pattern(regexp = "^[ㄱ-ㅎ가-힣]*$")
    @NotBlank
    private String username;

    @Pattern(regexp = "^[0-9]*$")
    @NotBlank
    private String phoneNumber;

    @Min(1)
    @NotNull
    private int correctCount;

    public QuizParticipants toQuizParticipants() {
        return QuizParticipants.builder()
                .phoneNumber(phoneNumber)
                .username(username)
                .correctCount(correctCount)
                .build();
    }

}
