package com.cstapin.documentation;

import com.cstapin.quiz.domain.LearningStatus;
import com.cstapin.quiz.domain.QuizCategoryStatus;
import com.cstapin.quiz.domain.QuizStatus;
import com.cstapin.quiz.service.QuizAdminService;
import com.cstapin.quiz.service.QuizUserService;
import com.cstapin.quiz.service.dto.*;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.time.YearMonth;
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
        문제_카테고리_삭제(getRequestSpecification("admin-delete-quiz-category").auth().oauth2(adminAccessToken), 1L);
    }

    @Test
    void selectDailyQuizzes() {
        //given
        DailyQuizzesSummaryResponse response = new DailyQuizzesSummaryResponse(3, 2,
                List.of(new QuizCategoryCountResponse("데이터베이스", 1),
                        new QuizCategoryCountResponse("운영체제", 2),
                        new QuizCategoryCountResponse("네트워크", 2)));

        //when
        when(quizUserService.selectDailyQuizzes(anyString())).thenReturn(response);

        //then
        오늘의_문제_선정(getRequestSpecification("user-select-daily-quizzes").auth().oauth2(userAccessToken));
    }

    @Test
    void findDailyQuizzes() {
        //given
        DailyQuizzesResponse response = new DailyQuizzesResponse(1L, LearningStatus.FAILURE, 1L,
                "데이터베이스", "인덱스", "+++은 기본 인덱스일 수도 있고 아닐 수도 있습니다.",
                "pk,기본키,기본 키");

        //when
        when(quizUserService.findDailyQuizzes(anyString())).thenReturn(List.of(response));

        //then
        오늘의_문제_목록_조회(getRequestSpecification("user-find-daily-quizzes").auth().oauth2(userAccessToken));
    }

    @Test
    void updateLearningRecordStatus() {
        //then
        문제_풀이_기록_등록(getRequestSpecification("user-update-learning-record-status").auth().oauth2(userAccessToken),
                1L, LearningStatus.SUCCESS);
    }

    @Test
    void findLearningRecords() {
        //given
        List<LearningRecordsResponse> response = List.of(new LearningRecordsResponse("데이터베이스", 3, 15),
                new LearningRecordsResponse("운영체제", 4, 12));

        //when
        when(quizUserService.findLearningRecords(anyString())).thenReturn(response);

        //then
        학습한_퀴즈_개수_조회(getRequestSpecification("user-find-learning-records").auth().oauth2(userAccessToken));
    }

    @Test
    void findQuizCategoriesByUser() {
        //given
        List<QuizCategoryResponse> quizCategoryResponses = List.of(
                new QuizCategoryResponse(1L, "운영체제", QuizCategoryStatus.PUBLIC),
                new QuizCategoryResponse(2L, "데이터베이스", QuizCategoryStatus.PRIVATE));

        //when
        when(quizUserService.findQuizCategories()).thenReturn(quizCategoryResponses);

        //then
        유저가_문제_카테고리_목록_조회(getRequestSpecification("user-find-quiz-categories").auth().oauth2(userAccessToken));
    }

    @Test
    void getRandomQuizzes() {
        //given
        List<RandomQuizzesResponse> response = List.of(
                new RandomQuizzesResponse(1L, "데이터베이스",
                        "인덱스", "+++은 기본 인덱스일 수도 있고 아닐 수도 있습니다.", List.of("cGs=", "6riw67O47YKk")),
                new RandomQuizzesResponse(2L, "운영체제",
                        "인덱스", "+++은 기본 인덱스일 수도 있고 아닐 수도 있습니다.", List.of("cGs=", "6riw67O47YKk")),
                new RandomQuizzesResponse(3L, "네트워크",
                        "인덱스", "+++은 기본 인덱스일 수도 있고 아닐 수도 있습니다.", List.of("cGs=", "6riw67O47YKk"))
        );

        //when
        when(quizUserService.getRandomQuizzes(any())).thenReturn(response);

        //then
        랜덤_문제_선정(getRequestSpecification("web-user-find-random-quizzes"), List.of(1L, 2L, 3L));
    }

    @Test
    void submitRandomQuizResult() {
        //given
        QuizParticipantsResponse 유기훈 = new QuizParticipantsResponse(1L, "010-****-5678",
                "유기훈", 49, LocalDateTime.of(2024, 1, 1, 12, 12, 12));

        //when
        when(quizUserService.saveOrUpdateQuizParticipants(any(), any())).thenReturn(유기훈);

        //then
        랜덤_문제_결과_등록(getRequestSpecification("web-user-submit-random-quiz-result"),
                "Dafdsfa12adf=",49, "01012345678", "유기훈");
    }

    @Test
    void getQuizParticipants() {
        //given
        QuizParticipantsResponse 유기룬 = new QuizParticipantsResponse(1L, "010-****-5678",
                "유*룬", 49, LocalDateTime.of(2024, 1, 1, 12, 12, 12));
        QuizParticipantsResponse 김만자 = new QuizParticipantsResponse(1L, "010-****-1234",
                "김*자", 49, LocalDateTime.of(2024, 1, 1, 12, 12, 12));

        //when
        when(quizUserService.getQuizParticipants(any())).thenReturn(new PageImpl<>(List.of(유기룬, 김만자),
                PageRequest.of(1, 10), 2L));

        //then
        랜덤_문제_유저_순위_목록_조회(getRequestSpecification("web-user-find-random-quiz-results"), "2024-01");
    }

    @Test
    void findQuizCategoriesInWeb() {
        //given
        List<QuizCategoryResponse> quizCategoryResponses = List.of(
                new QuizCategoryResponse(1L, "운영체제", QuizCategoryStatus.PUBLIC),
                new QuizCategoryResponse(2L, "데이터베이스", QuizCategoryStatus.PRIVATE));

        //when
        when(quizUserService.findQuizCategories()).thenReturn(quizCategoryResponses);

        //then
        웹에서_카테고리_목록_조회(getRequestSpecification("web-user-find-quiz-categories"));
    }
}
