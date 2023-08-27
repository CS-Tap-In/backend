package com.cstapin.auth.acceptance;

import com.cstapin.utils.AcceptanceTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static com.cstapin.auth.acceptance.QuizSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

public class QuizAcceptanceTest extends AcceptanceTest {

    /**
     * When: 문제를 등록한다.
     * Then: 문제 목록을 조회하면 조회된다.
     * Then: 문제 상세 조회하면 조회된다.
     */
    @Test
    void createQuiz() {
        //given
        ExtractableResponse<Response> 로그인 = AuthSteps.로그인_요청("admin", "password123@");
        String accessToken = 로그인.jsonPath().getString("accessToken");

        //when
        Map<String, Object> 문제_생성_요청값 = 문제_생성_요청값(1L, "인덱스", "+++은 기본 인덱스이다.", List.of("pk", "기본키", "기본 키"));
        ExtractableResponse<Response> 문제_생성_반환값 = 문제_생성(accessToken, 문제_생성_요청값);

        //then
        assertThat(문제_목록_조회(accessToken).jsonPath().getList("content.title")).containsExactly("인덱스");
        assertThat(문제_상세_조회(accessToken, 문제_생성_반환값.jsonPath().getLong("id")).jsonPath().getString("title")).isEqualTo("인덱스");
    }

    /**
     * 08-15
     * 앞으로 해야 하는 것
     * 1. 퀴즈 카테고리 인수 테스트 작성
     * 2. 퀴즈 카테고리 기능 구현
     * 3. 퀴즈 카테고리 문서 테스트 작성
     * 4. QuizRequest Validation Annotation 추가
     * 5. QuizRequestParams 에 검색 조건 추가 및 기능 구현
     */

    /**
     * When: 카테고리를 등록한다.
     * Then: 카테고리 목록을 조회하면 조회된다.
     */
    @Test
    void createQuizCategory() {
        //given
        ExtractableResponse<Response> 로그인 = AuthSteps.로그인_요청("admin", "password123@");
        String accessToken = 로그인.jsonPath().getString("accessToken");

        //when
        Map<String, String> 운영체제 = 문제_카테고리_요청값("운영체제", "PRIVATE");
        문제_카테고리_생성(accessToken, 운영체제);

        //then
        assertThat(문제_카테고리_목록_조회(accessToken).jsonPath().getList("quizCategories")).containsExactly("운영체제");
    }

    /**
     * Given: 문제를 등록한다.
     * When: 문제를 수정한다.
     * Then: 문제 상세 조회하면 수정된 문제가 조회된다.
     */
    @Test
    void updateQuiz() {
        //given

        //when

        //then
    }

    /**
     * Given: 문제를 등록한다.
     * When: 문제를 삭제한다.
     * Then: 문제 목록을 조회하면 조회되지 않는다.
     */
    @Test
    void deleteQuiz() {
        //given

        //when

        //then
    }

}
