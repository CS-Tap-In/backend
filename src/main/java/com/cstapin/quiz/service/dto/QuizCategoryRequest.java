package com.cstapin.quiz.service.dto;

import com.cstapin.quiz.domain.QuizCategory;
import com.cstapin.quiz.domain.QuizCategoryStatus;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
public class QuizCategoryRequest {

    @NotBlank
    @Size(max = 20)
    private String title;

    @NotNull
    private QuizCategoryStatus status;

    public QuizCategory toQuizCategory() {
        return new QuizCategory(title, status);
    }
}
