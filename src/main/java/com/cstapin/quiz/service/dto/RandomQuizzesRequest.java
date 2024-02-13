package com.cstapin.quiz.service.dto;

import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
public class RandomQuizzesRequest {

    @NotNull
    @Size(min = 3, max = 5)
    private List<Long> quizCategoryIds;

    private int size = 50;
}
