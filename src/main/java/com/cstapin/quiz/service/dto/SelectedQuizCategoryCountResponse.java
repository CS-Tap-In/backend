package com.cstapin.quiz.service.dto;

import lombok.Getter;

@Getter
public class SelectedQuizCategoryCountResponse {
    private final String quizCategoryTitle;
    private final int count;

    public SelectedQuizCategoryCountResponse(String quizCategoryTitle, int count) {
        this.quizCategoryTitle = quizCategoryTitle;
        this.count = count;
    }
}
