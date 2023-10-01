package com.cstapin.quiz.service.dto;

import lombok.Getter;

@Getter
public class QuizCategoryCountResponse {
    private final String quizCategoryTitle;
    private final int count;

    public QuizCategoryCountResponse(String quizCategoryTitle, int count) {
        this.quizCategoryTitle = quizCategoryTitle;
        this.count = count;
    }
}
