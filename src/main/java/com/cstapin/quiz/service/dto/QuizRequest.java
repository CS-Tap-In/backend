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

    public QuizRequest() {
    }

    public QuizRequest(Long categoryId, String title, String problem, List<String> answer) {
        this.categoryId = categoryId;
        this.title = title;
        this.problem = problem;
        this.answer = answer;
    }

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
