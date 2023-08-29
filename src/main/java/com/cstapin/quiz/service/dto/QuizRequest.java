package com.cstapin.quiz.service.dto;

import com.cstapin.member.domain.Member;
import com.cstapin.quiz.domain.QuizCategory;
import com.cstapin.quiz.domain.QuizStatus;
import com.cstapin.quiz.domain.Quiz;
import lombok.Getter;

import java.util.List;

@Getter
public class QuizRequest {

    private Long categoryId;
    private String title;
    private String problem;
    private List<String> answer;

    public Quiz toQuiz(Member author, QuizCategory quizCategory) {
        return Quiz.builder()
                .quizCategory(quizCategory)
                .author(author)
                .title(title)
                .problem(problem)
                .answer(String.join(",", answer))
                .status(QuizStatus.PRIVATE)
                .build();
    }
}
