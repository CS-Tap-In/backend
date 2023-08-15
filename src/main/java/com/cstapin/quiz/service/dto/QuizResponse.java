package com.cstapin.quiz.service.dto;

import com.cstapin.quiz.domain.Quiz;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
public class QuizResponse {

    private Long authorId;
    private String authorName;

    private Long categoryId;
    private String categoryTitle;

    private Long id;
    private String title;
    private String problem;
    private List<String> answer;
    private LocalDateTime createdAt;

    public static QuizResponse from(Quiz quiz) {
        return QuizResponse.builder()
                .authorId(quiz.getAuthor().getId())
                .authorName(quiz.getAuthor().getUsername())
                .id(quiz.getId())
                .title(quiz.getTitle())
                .problem(quiz.getProblem())
                .answer(List.of(quiz.getAnswer().split(",")))
                .createdAt(quiz.getCreatedAt())
                .build();
    }
}
