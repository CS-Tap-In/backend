package com.cstapin.quiz.service.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class QuizCountByCategoryId {
    private final String quizCategoryTitle;
    private final Long quizCategoryId;
    private final long count;

    @QueryProjection
    public QuizCountByCategoryId(String quizCategoryTitle, Long quizCategoryId, long count) {
        this.quizCategoryTitle = quizCategoryTitle;
        this.quizCategoryId = quizCategoryId;
        this.count = count;
    }
}
