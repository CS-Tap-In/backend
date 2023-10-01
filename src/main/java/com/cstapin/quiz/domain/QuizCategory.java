package com.cstapin.quiz.domain;

import com.cstapin.quiz.service.dto.QuizCategoryRequest;
import com.cstapin.support.domain.AbstractEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "quizCategory")
    @Where(clause = "status = 'PUBLIC' or status = 'PRIVATE' or status = 'UNAPPROVED'")
    private List<Quiz> quizzes = new ArrayList<>();

    public QuizCategory(String title, List<Quiz> quizzes) {
        this.title = title;
        this.status = QuizCategoryStatus.PUBLIC;
        this.quizzes = quizzes;
    }

    public QuizCategory(String title) {
        this(title, new ArrayList<>());
    }

    public void delete() {
        if (!quizzes.isEmpty()) {
            throw new IllegalStateException("공개, 숨김, 미인증 상태의 퀴즈가 속한 카테고리는 삭제 불가능합니다.");
        }
        this.status = QuizCategoryStatus.DELETED;
    }

    public void update(QuizCategoryRequest request) {
        this.title = request.getTitle();
    }
}
