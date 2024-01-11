package com.cstapin.quiz.service.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class RandomQuizzesRequest {

    private List<Long> quizCategoryIds;
}
