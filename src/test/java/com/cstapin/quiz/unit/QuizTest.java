package com.cstapin.quiz.unit;

import com.cstapin.quiz.domain.Quiz;
import org.junit.jupiter.api.Test;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class QuizTest {

    @Test
    void base64로_인코딩된_정답을_base64로_디코딩하면_정답_문자열과_일치한다() {
        //given
        Quiz quiz = Quiz.builder().answer("pk,기본키").build();

        //when
        List<String> decodedAnswers = quiz.getEncodedAnswers().stream()
                .map(encodedAnswer -> Base64.getDecoder().decode(encodedAnswer))
                .map(String::new).collect(Collectors.toList());

        //then
        assertThat(decodedAnswers).containsOnly("pk", "기본키");
    }
}
