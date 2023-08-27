package com.cstapin.quiz.service.dto;

import com.cstapin.quiz.domain.QuizCategoryStatus;
import lombok.Getter;

@Getter
public class QuizCategoryRequest {
    private String title;
    private QuizCategoryStatus status;
}
