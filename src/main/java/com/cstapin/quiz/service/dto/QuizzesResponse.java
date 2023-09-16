package com.cstapin.quiz.service.dto;

import com.cstapin.quiz.domain.Quiz;
import com.cstapin.quiz.domain.QuizStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class QuizzesResponse {
    private Long categoryId;
    private String categoryTitle;

    private Long id;
    private String title;
    private String problem;
    private QuizStatus status;
    private LocalDateTime createdAt;

    @QueryProjection
    @Builder
    public QuizzesResponse(Long categoryId, String categoryTitle, Long id, String title, String problem, QuizStatus status, LocalDateTime createdAt) {
        this.categoryId = categoryId;
        this.categoryTitle = categoryTitle;
        this.id = id;
        this.title = title;
        this.problem = problem;
        this.status = status;
        this.createdAt = createdAt;
    }

    public static QuizzesResponse from(Quiz quiz) {
        return QuizzesResponse.builder()
                .categoryId(quiz.getQuizCategory().getId())
                .categoryTitle(quiz.getQuizCategory().getTitle())
                .id(quiz.getId())
                .title(quiz.getTitle())
                .problem(quiz.getProblem())
                .status(quiz.getStatus())
                .createdAt(quiz.getCreatedAt())
                .build();
    }
}
