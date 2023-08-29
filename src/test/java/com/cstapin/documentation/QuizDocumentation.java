package com.cstapin.documentation;

import com.cstapin.quiz.service.QuizAdminService;
import com.cstapin.quiz.service.dto.QuizResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.cstapin.auth.acceptance.QuizSteps.문제_생성;
import static com.cstapin.auth.acceptance.QuizSteps.문제_생성_요청값;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class QuizDocumentation extends Documentation {

    @MockBean
    private QuizAdminService quizAdminService;

    @Test
    void createQuiz() {
        //given
        QuizResponse response = new QuizResponse(1L, "유기훈", 1L, "데이터베이스",
                1L, "인덱스", "+++은 기본 인덱스이다.", List.of("pk", "기본키", "기본 키"), LocalDateTime.now());

        //when
        when(quizAdminService.createQuiz(any(), anyString())).thenReturn(response);

        //then
        Map<String, Object> request = 문제_생성_요청값(1L, "인덱스", "+++은 기본 인덱스이다.", List.of("pk", "기본키", "기본 키"));
        문제_생성(getRequestSpecification("admin-create-quiz"), request);
    }
}
