package com.cstapin.quiz.service.dto;

import lombok.Getter;

@Getter
public class LearningRecordsResponse {
    private final String quizCategoryTitle;
    private final long learningQuizCount;
    private final long totalQuizCount;

    public LearningRecordsResponse(String quizCategoryTitle, long learningQuizCount, long totalQuizCount) {
        this.quizCategoryTitle = quizCategoryTitle;
        this.learningQuizCount = learningQuizCount;
        this.totalQuizCount = totalQuizCount;
    }
}
