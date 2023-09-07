package com.cstapin.quiz.service.dto;

import com.cstapin.quiz.domain.Quiz;
import com.cstapin.quiz.domain.QuizStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
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
    private QuizStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    public QuizResponse(Long authorId,
                        String authorName,
                        Long categoryId,
                        String categoryTitle,
                        Long id,
                        String title,
                        String problem,
                        List<String> answer,
                        QuizStatus status,
                        LocalDateTime createdAt) {
        this.authorId = authorId;
        this.authorName = authorName;
        this.categoryId = categoryId;
        this.categoryTitle = categoryTitle;
        this.id = id;
        this.status = status;
        this.title = title;
        this.problem = problem;
        this.answer = answer;
        this.createdAt = createdAt;
    }

    public static QuizResponse from(Quiz quiz) {
        return QuizResponse.builder()
                .authorId(quiz.getAuthor().getId())
                .authorName(quiz.getAuthor().getUsername())
                .id(quiz.getId())
                .status(quiz.getStatus())
                .title(quiz.getTitle())
                .problem(quiz.getProblem())
                .answer(List.of(quiz.getAnswer().split(",")))
                .createdAt(quiz.getCreatedAt())
                .build();
    }
}
