package com.cstapin.quiz.domain;

import com.cstapin.support.domain.AbstractEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "quiz_category")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuizCategory extends AbstractEntity {

    @Column(nullable = false, length = 20)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private QuizCategoryStatus status;

    public QuizCategory(String title, QuizCategoryStatus status) {
        this.title = title;
        this.status = status;
    }
}
