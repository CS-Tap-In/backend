package com.cstapin.quiz.domain;

import com.cstapin.member.domain.Member;
import com.cstapin.quiz.service.dto.QuizRequest;
import com.cstapin.support.domain.AbstractEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "quiz")
@Getter
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Quiz extends AbstractEntity {

    private static final int ANSWER_LENGTH = 500;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_category_id", nullable = false)
    private QuizCategory quizCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private Member author;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, length = 500)
    private String problem;

    @Column(nullable = false, length = 500)
    private String answer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuizStatus status;

    @Builder
    public Quiz(QuizCategory quizCategory, Member author, String title, String problem, String answer, QuizStatus status) {
        validateAnswerLength(answer);
        this.quizCategory = quizCategory;
        this.author = author;
        this.title = title;
        this.problem = problem;
        this.answer = answer;
        this.status = status;
    }

    public boolean matchQuizCategoryId(Long categoryId) {
        return Objects.equals(this.quizCategory.getId(), categoryId);
    }

    public void update(QuizRequest request) {
        String answer = String.join(",", request.getAnswer());
        validateAnswerLength(answer);
        this.title = request.getTitle();
        this.problem = request.getProblem();
        this.answer = answer;
    }

    public void update(QuizCategory quizCategory) {
        this.quizCategory = quizCategory;
    }

    private static void validateAnswerLength(String answer) {
        if (answer.length() > ANSWER_LENGTH) {
            throw new IllegalArgumentException("답의 길이가 너무 깁니다.");
        }
    }

    public void delete() {
        this.status = QuizStatus.DELETED;
    }

    public void changeStatus(QuizStatus status) {
        this.status = status;
    }
}
