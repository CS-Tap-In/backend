package com.cstapin.quiz.unit;

import com.cstapin.quiz.domain.LearningRecord;
import com.cstapin.quiz.domain.LearningStatus;
import com.cstapin.quiz.domain.Quiz;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class LearningRecordTest {

    @Test
    void 출제된_날이_같은지_확인한다() {
        //given
        LearningRecord 출제된문제 =
                new LearningRecord(1L, new Quiz(), LearningStatus.NONE, LocalDateTime.of(2023, 2, 1, 10, 10));

        //then
        assertThat(출제된문제.wasQuestionOnThisDay(LocalDate.of(2023, 2, 1))).isTrue();
        assertThat(출제된문제.wasQuestionOnThisDay(LocalDate.of(2023, 2, 2))).isFalse();
    }
}
