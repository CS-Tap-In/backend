package com.cstapin.quiz.service.dto;

import com.cstapin.quiz.domain.Quiz;
import lombok.Getter;

import java.util.List;

@Getter
public class RandomQuizzesResponse {
    private final Long quizId;
    private final String quizCategoryTitle;
    private final String quizTitle;
    private final String problem;
    private final List<String> answer;

    public RandomQuizzesResponse(Long quizId,
                                 String quizCategoryTitle,
                                 String quizTitle,
                                 String problem,
                                 List<String> answer) {
        this.quizId = quizId;
        this.quizCategoryTitle = quizCategoryTitle;
        this.quizTitle = quizTitle;
        this.problem = problem;
        this.answer = answer;
    }

    public static RandomQuizzesResponse from(Quiz quiz) {
        return new RandomQuizzesResponse(quiz.getId(), quiz.getQuizCategory().getTitle(), quiz.getTitle(),
                quiz.getProblem(), quiz.getEncodedAnswers());
    }
}
