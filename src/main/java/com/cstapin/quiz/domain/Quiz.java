package com.cstapin.quiz.domain;

import com.cstapin.member.domain.Member;
import com.cstapin.support.domain.AbstractEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "quiz")
@Getter
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
        if (String.join(",", answer).length() > ANSWER_LENGTH) {
            throw new IllegalArgumentException("답의 길이가 너무 깁니다.");
        }
        this.quizCategory = quizCategory;
        this.author = author;
        this.title = title;
        this.problem = problem;
        this.answer = answer;
        this.status = status;
    }
}
