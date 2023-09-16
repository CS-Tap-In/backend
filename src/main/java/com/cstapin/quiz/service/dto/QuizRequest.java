package com.cstapin.quiz.service.dto;

import com.cstapin.member.domain.Member;
import com.cstapin.quiz.domain.Quiz;
import com.cstapin.quiz.domain.QuizCategory;
import com.cstapin.quiz.domain.QuizStatus;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
public class QuizRequest {

    @NotNull
    private Long categoryId;
    @NotBlank
    @Size(max = 50)
    private String title;
    @Size(max = 500)
    @NotBlank
    private String problem;
    @NotNull
    @Size(min = 1)
    private List<String> answer;
    private QuizStatus status = QuizStatus.UNAPPROVED;

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
                .status(status)
                .build();
    }
}
