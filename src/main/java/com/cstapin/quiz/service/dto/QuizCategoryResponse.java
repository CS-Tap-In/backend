package com.cstapin.quiz.service.dto;

import com.cstapin.quiz.domain.QuizCategory;
import com.cstapin.quiz.domain.QuizCategoryStatus;
import lombok.Getter;

@Getter
public class QuizCategoryResponse {
    private final Long id;
    private final String title;
    private final QuizCategoryStatus status;

    public QuizCategoryResponse(Long id, String title, QuizCategoryStatus status) {
        this.id = id;
        this.title = title;
        this.status = status;
    }

    public static QuizCategoryResponse from(QuizCategory quizCategory) {
        return new QuizCategoryResponse(quizCategory.getId(), quizCategory.getTitle(), quizCategory.getStatus());
    }
}
