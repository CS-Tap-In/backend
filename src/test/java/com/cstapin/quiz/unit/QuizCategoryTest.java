package com.cstapin.quiz.unit;

import com.cstapin.quiz.domain.Quiz;
import com.cstapin.quiz.domain.QuizCategory;
import com.cstapin.quiz.domain.QuizCategoryStatus;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class QuizCategoryTest {

    @Test
    void 퀴즈_카테고리에_속하는_퀴즈가_없는_경우_삭제됨() {
        //given
        QuizCategory quizCategory = new QuizCategory("데이터베이스");

        //when
        quizCategory.delete();

        //then
        assertThat(quizCategory.getStatus()).isEqualTo(QuizCategoryStatus.DELETED);
    }

    @Test
    void 퀴즈_카테고리에_속하는_퀴즈가_있는_경우_예외() {
        //given
        QuizCategory quizCategory = new QuizCategory("데이터베이스", List.of(new Quiz()));

        //then
        assertThatThrownBy(quizCategory::delete).isInstanceOf(IllegalStateException.class);
    }
}
