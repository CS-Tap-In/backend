package com.cstapin.documentation;

import com.cstapin.quiz.domain.QuizCategoryStatus;
import com.cstapin.quiz.service.QuizAdminService;
import com.cstapin.quiz.service.dto.QuizCategoryResponse;
import com.cstapin.quiz.service.dto.QuizResponse;
import com.cstapin.quiz.service.dto.QuizzesResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.cstapin.auth.acceptance.AuthSteps.관리자_회원가입_요청;
import static com.cstapin.auth.acceptance.AuthSteps.로그인_요청;
import static com.cstapin.auth.acceptance.QuizSteps.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class QuizDocumentation extends Documentation {

    @MockBean
    private QuizAdminService quizAdminService;
    private String adminAccessToken;
    @Value("${props.join.admin}")
    private String joinAdminSecretKey;

    @BeforeEach
    void setUp() {
        관리자_회원가입_요청("admin", "Admin123@", "admin", joinAdminSecretKey);
        ExtractableResponse<Response> 로그인_응답 = 로그인_요청("admin", "Admin123@");
        adminAccessToken = 로그인_응답.jsonPath().getString("accessToken");
    }

    @Test
    void createQuiz() {
        //given
        QuizResponse response = new QuizResponse(1L, "유기훈", 1L, "데이터베이스",
                1L, "인덱스", "+++은 기본 인덱스이다.", List.of("pk", "기본키", "기본 키"), LocalDateTime.now());

        //when
        when(quizAdminService.createQuiz(any(), anyString())).thenReturn(response);

        //then
        Map<String, Object> request = 문제_생성_요청값(1L, "인덱스", "+++은 기본 인덱스이다.", List.of("pk", "기본키", "기본 키"));
        문제_생성(getRequestSpecification("admin-create-quiz").auth().oauth2(adminAccessToken), request);
    }

    @Test
    void findQuiz() {
        //given
        QuizResponse response = new QuizResponse(1L, "유기훈", 1L, "데이터베이스",
                1L, "인덱스", "+++은 기본 인덱스이다.", List.of("pk", "기본키", "기본 키"), LocalDateTime.now());

        //when
        when(quizAdminService.findQuiz(any())).thenReturn(response);

        //then
        문제_상세_조회(getRequestSpecification("admin-find-quiz-details").auth().oauth2(adminAccessToken), 1L);
    }

    @Test
    void findQuizzes() {
        //given
        QuizzesResponse response = QuizzesResponse.builder().categoryId(1L).categoryTitle("데이터베이스").id(1L)
                .title("인덱스").problem("+++은 기본 인덱스이다.").createdAt(LocalDateTime.now()).build();

        Map<String, String> params = 문제_목록_조회_요청값("author", "유기훈", 1L);

        //when
        when(quizAdminService.findQuizzes(any())).thenReturn(new PageImpl<>(List.of(response), PageRequest.of(0, 10), 1L));

        //then
        문제_목록_조회(getRequestSpecification("admin-find-quizzes").auth().oauth2(adminAccessToken), params);
    }

    @Test
    void createQuizCategory() {
        //given
        QuizCategoryResponse response = new QuizCategoryResponse("데이터베이스", QuizCategoryStatus.PUBLIC);

        //when
        when(quizAdminService.createQuizCategory(any())).thenReturn(response);

        //then
        Map<String, String> request = 문제_카테고리_요청값("데이터베이스");
        문제_카테고리_생성(getRequestSpecification("admin-create-quiz-category").auth().oauth2(adminAccessToken), request);
    }

    @Test
    void findQuizCategories() {
        //given
        List<QuizCategoryResponse> quizCategoryResponses = List.of(
                new QuizCategoryResponse("운영체제", QuizCategoryStatus.PUBLIC),
                new QuizCategoryResponse("데이터베이스", QuizCategoryStatus.PRIVATE));

        //when
        when(quizAdminService.findQuizCategory()).thenReturn(quizCategoryResponses);

        //then
        문제_카테고리_목록_조회(getRequestSpecification("admin-find-quiz-categories").auth().oauth2(adminAccessToken));
    }
}
