package com.cstapin.quiz.service.dto;

import com.cstapin.quiz.domain.QuizStatus;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
public class QuizzesStatusRequest {

    @NotNull
    @Size(min = 1)
    private List<Long> quizIds;

    @NotNull
    private QuizStatus status;
}
