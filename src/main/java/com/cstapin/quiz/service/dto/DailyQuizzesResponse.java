package com.cstapin.quiz.service.dto;

import com.cstapin.quiz.domain.LearningStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public class DailyQuizzesResponse {
    private final Long learningRecordId;
    private final LearningStatus learningStatus;
    private final Long quizId;
    private final String quizCategoryTitle;
    private final String quizTitle;
    private final String problem;
    private final List<String> answer;

    @QueryProjection
    public DailyQuizzesResponse(Long learningRecordId,
                                LearningStatus learningStatus,
                                Long quizId,
                                String quizCategoryTitle,
                                String quizTitle,
                                String problem,
                                String answer) {
        this.learningRecordId = learningRecordId;
        this.learningStatus = learningStatus;
        this.quizId = quizId;
        this.quizCategoryTitle = quizCategoryTitle;
        this.quizTitle = quizTitle;
        this.problem = problem;
        this.answer = List.of(answer.split(","));
    }
}
