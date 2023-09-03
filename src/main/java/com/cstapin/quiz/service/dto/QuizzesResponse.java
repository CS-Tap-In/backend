package com.cstapin.quiz.service.dto;

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
    private LocalDateTime createdAt;

    @QueryProjection
    public QuizzesResponse(Long categoryId, String categoryTitle, Long id, String title, String problem, LocalDateTime createdAt) {
        this.categoryId = categoryId;
        this.categoryTitle = categoryTitle;
        this.id = id;
        this.title = title;
        this.problem = problem;
        this.createdAt = createdAt;
    }
}
