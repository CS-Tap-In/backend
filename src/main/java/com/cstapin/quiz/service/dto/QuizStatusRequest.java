package com.cstapin.quiz.service.dto;

import com.cstapin.quiz.domain.QuizStatus;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class QuizStatusRequest {

    @NotNull
    private QuizStatus status;
}
