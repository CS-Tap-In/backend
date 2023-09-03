package com.cstapin.quiz.service.dto;

import com.cstapin.quiz.domain.QuizCategoryStatus;
import lombok.Getter;

@Getter
public class QuizCategoryResponse {
    private final String title;
    private final QuizCategoryStatus status;

    public QuizCategoryResponse(String title, QuizCategoryStatus status) {
        this.title = title;
        this.status = status;
    }
}
