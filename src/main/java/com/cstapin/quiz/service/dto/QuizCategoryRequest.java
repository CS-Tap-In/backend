package com.cstapin.quiz.service.dto;

import com.cstapin.quiz.domain.QuizCategory;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class QuizCategoryRequest {

    @NotBlank
    @Size(max = 20)
    private String title;

    public QuizCategory toQuizCategory() {
        return new QuizCategory(title);
    }
}
