package com.cstapin.quiz.domain;

import com.cstapin.support.domain.AbstractEntity;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@Getter
public class QuizCategory extends AbstractEntity {

    @Column(nullable = false, length = 20)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private QuizCategoryStatus status;

}
