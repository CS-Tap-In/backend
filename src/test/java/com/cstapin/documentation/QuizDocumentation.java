package com.cstapin.documentation;

import com.cstapin.quiz.domain.QuizCategoryStatus;
import com.cstapin.quiz.domain.QuizStatus;
import com.cstapin.quiz.service.QuizAdminService;
import com.cstapin.quiz.service.QuizUserService;
import com.cstapin.quiz.service.dto.QuizCategoryResponse;
import com.cstapin.quiz.service.dto.QuizResponse;
import com.cstapin.quiz.service.dto.QuizzesResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.cstapin.auth.acceptance.AuthSteps.로그인_요청;
import static com.cstapin.quiz.acceptance.QuizSteps.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class QuizDocumentation extends Documentation {

    @MockBean
    private QuizUserService quizUserService;
    @MockBean
    private QuizAdminService quizAdminService;
    private String adminAccessToken;
    private String userAccessToken;

    @BeforeEach
    void setUp() {
        ExtractableResponse<Response> 로그인_응답 = 로그인_요청("admin", "password123@");
        adminAccessToken = 로그인_응답.jsonPath().getString("accessToken");

        ExtractableResponse<Response> 유저_로그인_응답 = 로그인_요청("user", "password123@");
        userAccessToken = 유저_로그인_응답.jsonPath().getString("accessToken");
    }

    @Test
    void createQuiz() {
        //given
        QuizResponse response = new QuizResponse(1L, "유기훈", 1L, "데이터베이스",
                1L, "인덱스", "+++은 기본 인덱스이다.", List.of("pk", "기본키", "기본 키"),
                QuizStatus.PRIVATE, LocalDateTime.now());

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
                1L, "인덱스", "+++은 기본 인덱스이다.", List.of("pk", "기본키", "기본 키"),
                QuizStatus.PRIVATE, LocalDateTime.now());

        //when
        when(quizAdminService.findQuiz(any())).thenReturn(response);

        //then
        문제_상세_조회(getRequestSpecification("admin-find-quiz-details").auth().oauth2(adminAccessToken), 1L);
    }

    @Test
    void findQuizzes() {
        //given
        QuizzesResponse response = QuizzesResponse.builder().categoryId(1L).categoryTitle("데이터베이스").id(1L)
                .title("인덱스").problem("+++은 기본 인덱스이다.").status(QuizStatus.PRIVATE).createdAt(LocalDateTime.now()).build();

        Map<String, String> params = 문제_목록_조회_요청값("author", "유기훈", 1L);

        //when
        when(quizAdminService.findQuizzes(any())).thenReturn(new PageImpl<>(List.of(response), PageRequest.of(0, 10), 1L));

        //then
        문제_목록_조회(getRequestSpecification("admin-find-quizzes").auth().oauth2(adminAccessToken), params);
    }

    @Test
    void createQuizCategory() {
        //given
        QuizCategoryResponse response = new QuizCategoryResponse(1L, "데이터베이스", QuizCategoryStatus.PUBLIC);

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
                new QuizCategoryResponse(1L, "운영체제", QuizCategoryStatus.PUBLIC),
                new QuizCategoryResponse(2L, "데이터베이스", QuizCategoryStatus.PRIVATE));

        //when
        when(quizAdminService.findQuizCategories()).thenReturn(quizCategoryResponses);

        //then
        문제_카테고리_목록_조회(getRequestSpecification("admin-find-quiz-categories").auth().oauth2(adminAccessToken));
    }

    @Test
    void updateQuiz() {
        //given
        QuizResponse response = new QuizResponse(1L, "유기훈", 1L, "데이터베이스",
                1L, "인덱스", "+++은 기본 인덱스일 수도 있고 아닐 수도 있습니다.", List.of("pk", "기본키", "기본 키"),
                QuizStatus.PRIVATE, LocalDateTime.now());

        //when
        when(quizAdminService.updateQuiz(any(), anyLong())).thenReturn(response);

        //then
        Map<String, Object> request = 문제_생성_요청값(1L, "인덱스", "+++은 기본 인덱스일 수도 있고 아닐 수도 있습니다.", List.of("pk", "기본키", "기본 키"));
        문제_수정(getRequestSpecification("admin-update-quiz").auth().oauth2(adminAccessToken), request, 1L);
    }

    @Test
    void deleteQuiz() {
        //then
        문제_삭제(getRequestSpecification("admin-delete-quiz").auth().oauth2(adminAccessToken), 1L);
    }

    @Test
    void changeStatusOfQuiz() {
        //given
        QuizResponse response = new QuizResponse(1L, "유기훈", 1L, "데이터베이스",
                1L, "인덱스", "+++은 기본 인덱스일 수도 있고 아닐 수도 있습니다.", List.of("pk", "기본키", "기본 키"),
                QuizStatus.PUBLIC, LocalDateTime.now());

        //when
        when(quizAdminService.changeStatusOfQuiz(any(), any())).thenReturn(response);

        //then
        문제_상태_변경(getRequestSpecification("admin-change-status-quiz").auth().oauth2(adminAccessToken), 1L, "PUBLIC");
    }

    @Test
    void changeStatusOfQuizzes() {
        //then
        문제들_상태_변경(getRequestSpecification("admin-change-status-quizzes").auth().oauth2(adminAccessToken), List.of(1L, 2L, 3L), "PRIVATE");
    }

    @Test
    void createQuizByUser() {
        //given
        QuizResponse response = new QuizResponse(1L, "유기훈", 1L, "데이터베이스",
                1L, "인덱스", "+++은 기본 인덱스일 수도 있고 아닐 수도 있습니다.", List.of("pk", "기본키", "기본 키"),
                QuizStatus.UNAPPROVED, LocalDateTime.now());

        //when
        when(quizUserService.createQuiz(any(), anyString())).thenReturn(response);

        //then
        Map<String, Object> request = 유저가_문제_생성_요청값(1L, "인덱스", "+++은 기본 인덱스이다.", List.of("pk", "기본키", "기본 키"));
        유저가_문제_생성(getRequestSpecification("user-create-quiz").auth().oauth2(userAccessToken), request);
    }

    @Test
    void findQuizzesByAuthor() {
        //given
        QuizzesResponse response = QuizzesResponse.builder().categoryId(1L).categoryTitle("데이터베이스").id(1L)
                .title("인덱스").problem("+++은 기본 인덱스이다.").status(QuizStatus.UNAPPROVED).createdAt(LocalDateTime.now()).build();

        //when
        when(quizUserService.findQuizzesByAuthor(anyString(), any()))
                .thenReturn(new PageImpl<>(List.of(response), PageRequest.of(1, 10), 1L));

        //then
        내가_만든_문제_목록_조회(getRequestSpecification("user-find-quiz-by-author").auth().oauth2(userAccessToken));
    }

    @Test
    void updateQuizCategory() {
        //given
        QuizCategoryResponse response = new QuizCategoryResponse(1L, "데이터베이스", QuizCategoryStatus.PUBLIC);

        //when
        when(quizAdminService.updateQuizCategory(anyLong(), any())).thenReturn(response);

        //then
        Map<String, String> request = 문제_카테고리_요청값("데이터베이스");
        문제_카테고리_수정(getRequestSpecification("admin-update-quiz-category").auth().oauth2(adminAccessToken), 1L, request);
    }

    @Test
    void deleteQuizCategory() {
        //then
        문제_카테고리_삭제(getRequestSpecification("admin-update-quiz-category").auth().oauth2(adminAccessToken), 1L);
    }

}
