package com.cstapin.quiz.service.dto;

import com.cstapin.quiz.domain.Quiz;
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

    public static QuizzesResponse of(Quiz quiz) {
        return QuizzesResponse.builder()
                .id(quiz.getId())
                .title(quiz.getTitle())
                .problem(quiz.getProblem())
                .createdAt(quiz.getCreatedAt())
                .build();
    }
}
