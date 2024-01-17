package com.cstapin.quiz.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class QuizParticipantsTest {

    @Test
    void 핸드폰번호는_가운데_번호가_마스킹되고_하이픈이_붙는다() {
        //given
        QuizParticipants quizParticipants = QuizParticipants.builder().phoneNumber("01012345678").build();

        //then
        assertThat(quizParticipants.getMaskedPhoneNumber()).isEqualTo("010-****-5678");
    }

    @Test
    void 이름이_3글자_이상인_경우_첫글자_마지막글자를_제외하고는_마스킹된다() {
        //given
        QuizParticipants 유기훈 = QuizParticipants.builder().username("유기훈").build();
        QuizParticipants 제갈기훈 = QuizParticipants.builder().username("제갈기훈").build();

        //then
        assertThat(유기훈.getMaskedUsername()).isEqualTo("유*훈");
        assertThat(제갈기훈.getMaskedUsername()).isEqualTo("제**훈");
    }

    @Test
    void 이름이_2글자인_경우_마지막글자만_마스킹된다() {
        //given
        QuizParticipants 유기 = QuizParticipants.builder().username("유기").build();

        //then
        assertThat(유기.getMaskedUsername()).isEqualTo("유*");
    }
}