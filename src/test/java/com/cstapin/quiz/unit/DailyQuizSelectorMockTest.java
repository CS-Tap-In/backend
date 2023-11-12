package com.cstapin.quiz.unit;

import com.cstapin.quiz.domain.LearningRecord;
import com.cstapin.quiz.domain.LearningStatus;
import com.cstapin.quiz.domain.Quiz;
import com.cstapin.quiz.service.DailyQuizSelector;
import com.cstapin.quiz.service.query.LearningRecordQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DailyQuizSelectorMockTest {

    private static final LocalDateTime now = LocalDateTime.of(2023, 2, 10, 11, 11);

    @Mock
    private LearningRecordQueryService learningRecordQueryService;

    private DailyQuizSelector dailyQuizSelector;
    private static final Long memberId = 1L;
    private static final int dailyGoal = 5;
    private Quiz 최근에틀린퀴즈1 = new Quiz();
    private Quiz 최근에틀린퀴즈2 = new Quiz();
    private Quiz 최근에틀린퀴즈3 = new Quiz();
    private Quiz 이전에틀린퀴즈1 = new Quiz();
    private Quiz 최근에맞은퀴즈1 = new Quiz();
    private Quiz 최근에맞은퀴즈2 = new Quiz();
    private Quiz 푼적없는퀴즈1 = new Quiz();
    private Quiz 푼적없는퀴즈2 = new Quiz();
    private Quiz 푼적없는퀴즈3 = new Quiz();

    @BeforeEach
    void setUpFixture() {
        when(learningRecordQueryService.findLatestLearningRecords(anyLong(), anyInt())).thenReturn(
                List.of(
                        new LearningRecord(1L, 최근에틀린퀴즈1, LearningStatus.FAILURE, now.minusDays(1)),
                        new LearningRecord(1L, 최근에틀린퀴즈2, LearningStatus.FAILURE, now.minusDays(1)),
                        new LearningRecord(1L, 이전에틀린퀴즈1, LearningStatus.FAILURE, now.minusDays(2)),
                        new LearningRecord(1L, 최근에맞은퀴즈1, LearningStatus.SUCCESS, now.minusDays(1)),
                        new LearningRecord(1L, 최근에맞은퀴즈2, LearningStatus.SUCCESS, now.minusDays(1)))
        );

        dailyQuizSelector = new DailyQuizSelector(learningRecordQueryService);
    }

    @Test
    void 제일_최근에_풀었을_때_틀린_문제들은_모두_뽑는다() {
        //when
        Set<Quiz> quizzes = dailyQuizSelector.select(memberId, dailyGoal).getTotalQuizzes();

        //then
        assertThat(quizzes).contains(최근에틀린퀴즈1, 최근에틀린퀴즈2);
    }

    @Test
    void 사분의삼_비율이_안되면_이전에_틀린_문제들을_뽑아온다() {
        //when
        Set<Quiz> quizzes = dailyQuizSelector.select(memberId, dailyGoal).getTotalQuizzes();

        //then
        assertThat(quizzes).contains(최근에틀린퀴즈1, 최근에틀린퀴즈2, 이전에틀린퀴즈1);
    }

    @Test
    void 사분의삼_비율이_되면_이전에_틀린_문제들을_뽑아오지않는다() {
        when(learningRecordQueryService.findLatestLearningRecords(anyLong(), anyInt())).thenReturn(
                List.of(
                        new LearningRecord(1L, 최근에틀린퀴즈1, LearningStatus.FAILURE, now.minusDays(1)),
                        new LearningRecord(1L, 최근에틀린퀴즈2, LearningStatus.FAILURE, now.minusDays(1)),
                        new LearningRecord(1L, 최근에틀린퀴즈3, LearningStatus.FAILURE, now.minusDays(1)),
                        new LearningRecord(1L, 최근에맞은퀴즈1, LearningStatus.SUCCESS, now.minusDays(1)),
                        new LearningRecord(1L, 최근에맞은퀴즈2, LearningStatus.SUCCESS, now.minusDays(1)),
                        new LearningRecord(1L, 이전에틀린퀴즈1, LearningStatus.FAILURE, now.minusDays(2)))
        );

        //when
        Set<Quiz> quizzes = dailyQuizSelector.select(memberId, dailyGoal).getTotalQuizzes();

        //then
        assertThat(quizzes).contains(최근에틀린퀴즈1, 최근에틀린퀴즈2, 최근에틀린퀴즈3);
        assertThat(quizzes).doesNotContain(이전에틀린퀴즈1);
    }

    @Test
    void 새로운_문제가_충분히_있을_떄_제일_최근에_풀었을_때_맞은_문제들은_선택되지_않는다() {
        //given
        when(learningRecordQueryService.findUnSolvedQuiz(memberId)).thenReturn(
                List.of(푼적없는퀴즈1, 푼적없는퀴즈2)
        );

        //when
        Set<Quiz> quizzes = dailyQuizSelector.select(memberId, dailyGoal).getTotalQuizzes();

        //then
        assertThat(quizzes).contains(최근에틀린퀴즈1, 최근에틀린퀴즈2, 이전에틀린퀴즈1, 푼적없는퀴즈1, 푼적없는퀴즈2);
    }

    @Test
    void 새로운_문제가_없을_떄_제일_최근에_풀었을_때_맞은_문제들도_선택된다() {
        //given
        when(learningRecordQueryService.findUnSolvedQuiz(memberId)).thenReturn(List.of());

        //when
        Set<Quiz> quizzes = dailyQuizSelector.select(memberId, dailyGoal).getTotalQuizzes();

        //then
        assertThat(quizzes).contains(최근에틀린퀴즈1, 최근에틀린퀴즈2, 이전에틀린퀴즈1, 최근에맞은퀴즈1, 최근에맞은퀴즈2);
    }

    @Test
    void 전에_틀렸는데_직전에는_맞은_문제는_선택되지_않는다() {
        //given
        when(learningRecordQueryService.findLatestLearningRecords(anyLong(), anyInt())).thenReturn(
                List.of(
                        new LearningRecord(1L, 최근에틀린퀴즈1, LearningStatus.FAILURE, now.minusDays(1)),
                        new LearningRecord(1L, 최근에틀린퀴즈2, LearningStatus.FAILURE, now.minusDays(1)),
                        new LearningRecord(1L, 최근에맞은퀴즈1, LearningStatus.SUCCESS, now.minusDays(1)),
                        new LearningRecord(1L, 최근에맞은퀴즈2, LearningStatus.SUCCESS, now.minusDays(1)),
                        new LearningRecord(1L, 이전에틀린퀴즈1, LearningStatus.FAILURE, now.minusDays(2)),
                        new LearningRecord(1L, 이전에틀린퀴즈1, LearningStatus.SUCCESS, now.minusDays(1)))
        );

        when(learningRecordQueryService.findUnSolvedQuiz(memberId)).thenReturn(
                List.of(푼적없는퀴즈1, 푼적없는퀴즈2, 푼적없는퀴즈3)
        );

        //when
        Set<Quiz> quizzes = dailyQuizSelector.select(memberId, dailyGoal).getTotalQuizzes();

        //then
        assertThat(quizzes).contains(최근에틀린퀴즈1, 최근에틀린퀴즈2, 푼적없는퀴즈1, 푼적없는퀴즈2, 푼적없는퀴즈3);
        assertThat(quizzes).doesNotContain(이전에틀린퀴즈1);
    }

    @Test
    void 최근_두번_연속으로_맞은_문제가_있어도_문제가_부족하면_선택된다() {
        //given
        when(learningRecordQueryService.findLatestLearningRecords(anyLong(), anyInt())).thenReturn(
                List.of(
                        new LearningRecord(1L, 최근에맞은퀴즈1, LearningStatus.SUCCESS, now.minusDays(2)),
                        new LearningRecord(1L, 최근에맞은퀴즈2, LearningStatus.SUCCESS, now.minusDays(2)),
                        new LearningRecord(1L, 최근에맞은퀴즈1, LearningStatus.SUCCESS, now.minusDays(1)),
                        new LearningRecord(1L, 최근에맞은퀴즈2, LearningStatus.SUCCESS, now.minusDays(1)))
        );

        when(learningRecordQueryService.findUnSolvedQuiz(memberId)).thenReturn(
                List.of()
        );

        //when
        Set<Quiz> quizzes = dailyQuizSelector.select(memberId, dailyGoal).getTotalQuizzes();

        //then
        assertThat(quizzes).contains(최근에맞은퀴즈1, 최근에맞은퀴즈2);
    }

}
