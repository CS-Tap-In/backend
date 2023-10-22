package com.cstapin.quiz.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LearningRecordTest {

    private final Long memberId = 1L;

    @Test
    void NONE에서_SUCCESS로_변경_가능하다() {
        //given
        LearningRecord learningRecord = new LearningRecord(memberId, new Quiz(), LearningStatus.NONE, LocalDateTime.now());

        //when
        learningRecord.updateStatus(memberId, LearningStatus.SUCCESS);

        //then
        assertThat(learningRecord.getStatus()).isEqualTo(LearningStatus.SUCCESS);
    }

    @Test
    void NONE에서_FAILURE로_변경_가능하다() {
        //given
        LearningRecord learningRecord = new LearningRecord(memberId, new Quiz(), LearningStatus.NONE, LocalDateTime.now());

        //when
        learningRecord.updateStatus(memberId, LearningStatus.FAILURE);

        //then
        assertThat(learningRecord.getStatus()).isEqualTo(LearningStatus.FAILURE);
    }

    @Test
    void FAILURE에서_RECOVERY로_변경_가능하다() {
        //given
        LearningRecord learningRecord = new LearningRecord(memberId, new Quiz(), LearningStatus.FAILURE, LocalDateTime.now());

        //when
        learningRecord.updateStatus(memberId, LearningStatus.RECOVERY);

        //then
        assertThat(learningRecord.getStatus()).isEqualTo(LearningStatus.RECOVERY);
    }

    @Test
    void NONE에서_RECOVERY로_변경_불가능하다() {
        //given
        LearningRecord learningRecord = new LearningRecord(memberId, new Quiz(), LearningStatus.NONE, LocalDateTime.now());

        //then
        assertThatThrownBy(() -> learningRecord.updateStatus(memberId, LearningStatus.RECOVERY))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void FALIURE에서_SUCCESS로_변경_불가능하다() {
        //given
        LearningRecord learningRecord = new LearningRecord(memberId, new Quiz(), LearningStatus.FAILURE, LocalDateTime.now());

        //then
        assertThatThrownBy(() -> learningRecord.updateStatus(memberId, LearningStatus.SUCCESS))
                .isInstanceOf(IllegalStateException.class);
    }

}