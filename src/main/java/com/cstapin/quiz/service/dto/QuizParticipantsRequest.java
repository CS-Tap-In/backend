package com.cstapin.quiz.service.dto;

import com.cstapin.quiz.domain.QuizParticipants;
import lombok.Getter;

import javax.validation.constraints.*;

@Getter
public class QuizParticipantsRequest {

    @Pattern(regexp = "^[ㄱ-ㅎ가-힣]*$")
    @NotBlank
    @Size(min = 2, max = 10)
    private String username;

    @Pattern(regexp = "^[0-9]*$")
    @NotBlank
    @Size(min = 11, max = 11)
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
