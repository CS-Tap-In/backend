package com.cstapin.quiz.unit;

import com.cstapin.quiz.domain.Quiz;
import com.cstapin.quiz.domain.QuizCategory;
import com.cstapin.quiz.domain.QuizRepository;
import com.cstapin.quiz.service.RandomQuizSelector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RandomQuizSelectorMockTest {

    @Mock
    private QuizRepository quizRepository;

    private RandomQuizSelector randomQuizSelector;
    private final QuizCategory 데이터베이스 = new QuizCategory("데이터베이스");
    private final QuizCategory 네트워크 = new QuizCategory("네트워크");
    private final QuizCategory 운영체제 = new QuizCategory("운영체제");
    private final Quiz 데이터베이스_퀴즈1 = Quiz.builder().quizCategory(데이터베이스).build();
    private final Quiz 데이터베이스_퀴즈2 = Quiz.builder().quizCategory(데이터베이스).build();
    private final Quiz 네트워크_퀴즈1 = Quiz.builder().quizCategory(네트워크).build();
    private final Quiz 네트워크_퀴즈2 = Quiz.builder().quizCategory(네트워크).build();
    private final Quiz 운영체제_퀴즈1 = Quiz.builder().quizCategory(운영체제).build();
    private final Quiz 운영체제_퀴즈2 = Quiz.builder().quizCategory(운영체제).build();

    @BeforeEach
    void setUp() {
        randomQuizSelector = new RandomQuizSelector(quizRepository);
    }

    @Test
    void 카테고리당_적어도_하나이상의_문제가_선정된다() {
        //given
        Long 데이터베이스_카테고리_id = 1L;
        Long 네트워크_카테고리_id = 2L;
        Long 운영체제_카테고리_id = 3L;

        when(quizRepository.findByQuizCategoryId(데이터베이스_카테고리_id))
                .thenReturn(List.of(데이터베이스_퀴즈1, 데이터베이스_퀴즈2));
        when(quizRepository.findByQuizCategoryId(네트워크_카테고리_id))
                .thenReturn(List.of(네트워크_퀴즈1, 네트워크_퀴즈2));
        when(quizRepository.findByQuizCategoryId(운영체제_카테고리_id))
                .thenReturn(List.of(운영체제_퀴즈1, 운영체제_퀴즈2));

        //when
        List<Quiz> selectedQuizzes = randomQuizSelector.select(List.of(1L, 2L, 3L), (objects, count) -> objects);

        //then
        List<QuizCategory> selectedQuizCategories = selectedQuizzes.stream().map(Quiz::getQuizCategory)
                .collect(Collectors.toList());
        assertThat(selectedQuizCategories).contains(데이터베이스, 네트워크, 운영체제);
    }

    @Test
    void 문제는_중복되서_선택되지_않는다() {
        //given
        Long 데이터베이스_카테고리_id = 1L;
        Long 네트워크_카테고리_id = 2L;
        Long 운영체제_카테고리_id = 3L;

        when(quizRepository.findByQuizCategoryId(데이터베이스_카테고리_id))
                .thenReturn(List.of(데이터베이스_퀴즈1, 데이터베이스_퀴즈2));
        when(quizRepository.findByQuizCategoryId(네트워크_카테고리_id))
                .thenReturn(List.of(네트워크_퀴즈1, 네트워크_퀴즈2));
        when(quizRepository.findByQuizCategoryId(운영체제_카테고리_id))
                .thenReturn(List.of(운영체제_퀴즈1, 운영체제_퀴즈2));

        //when
        List<Quiz> selectedQuizzes = randomQuizSelector.select(List.of(1L, 2L, 3L), (objects, count) -> objects);

        //then
        assertThat(selectedQuizzes.size()).isEqualTo(new HashSet<>(selectedQuizzes).size());
    }
}
