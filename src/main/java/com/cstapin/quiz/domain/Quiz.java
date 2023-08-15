package com.cstapin.quiz.domain;

import com.cstapin.member.domain.Member;
import com.cstapin.member.domain.QuizStatus;
import com.cstapin.support.domain.AbstractEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Quiz extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private Member author;

    private String title;

    private String problem;

    private String answer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuizStatus status;

    @Builder
    public Quiz(Member author, String title, String problem, String answer, QuizStatus status) {
        this.author = author;
        this.title = title;
        this.problem = problem;
        this.answer = answer;
        this.status = status;
    }
}
