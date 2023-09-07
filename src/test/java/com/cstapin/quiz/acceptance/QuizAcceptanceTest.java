package com.cstapin.quiz.acceptance;

import com.cstapin.auth.acceptance.AuthSteps;
import com.cstapin.utils.AcceptanceTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static com.cstapin.quiz.acceptance.QuizSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

public class QuizAcceptanceTest extends AcceptanceTest {

    private String accessToken;
    private Long 문제_id;

    @BeforeEach
    void setUpFixture() {
        //로그인
        ExtractableResponse<Response> 로그인 = AuthSteps.로그인_요청("admin", "password123@");
        accessToken = 로그인.jsonPath().getString("accessToken");

        //카테고리
        문제_카테고리_생성(accessToken, 문제_카테고리_요청값("데이터베이스"));

        //문제 생성
        Map<String, Object> 문제_생성_요청값 = 문제_생성_요청값(1L, "인덱스", "+++은 기본 인덱스이다.", List.of("pk", "기본키", "기본 키"));
        문제_id = 문제_생성(accessToken, 문제_생성_요청값).jsonPath().getLong("id");
    }

    /**
     * When: 문제를 등록한다.
     * Then: 문제 목록을 조회하면 조회된다.
     * Then: 문제 상세 조회하면 조회된다.
     */
    @Test
    void createQuiz() {
        //then
        assertThat(문제_목록_조회(accessToken, 문제_목록_조회_요청값("author", "admin", 1L)).jsonPath().getList("content.title")).containsExactly("인덱스");
        assertThat(문제_상세_조회(accessToken, 문제_id).jsonPath().getString("title")).isEqualTo("인덱스");
    }

    /**
     * When: 카테고리를 등록한다.
     * Then: 카테고리 목록을 조회하면 조회된다.
     */
    @Test
    void createQuizCategory() {
        //then
        assertThat(문제_카테고리_목록_조회(accessToken).jsonPath().getString("[0].title")).isEqualTo("데이터베이스");
    }

    /**
     * Given: 문제를 등록한다.
     * When: 문제를 수정한다.
     * Then: 문제 상세 조회하면 수정된 문제가 조회된다.
     */
    @Test
    void updateQuiz() {
        //when
        Map<String, Object> 문제_생성_요청값 = 문제_생성_요청값(1L, "index", "+++은 기본 인덱스이다.", List.of("pk", "기본키", "기본 키"));
        문제_수정(accessToken, 문제_생성_요청값, 문제_id);

        //then
        assertThat(문제_상세_조회(accessToken, 문제_id).jsonPath().getString("title")).isEqualTo("index");
    }

    /**
     * Given: 문제를 등록한다.
     * When: 문제를 삭제한다.
     * Then: 문제 목록을 조회하면 조회되지 않는다.
     */
    @Test
    void deleteQuiz() {
        //when
        문제_삭제(accessToken, 문제_id);

        //then
        assertThat(문제_목록_조회(accessToken, 문제_목록_조회_요청값("author", "admin", 1L)).jsonPath().getList("content")).isEmpty();
    }

    /**
     * Given: 미승인 상태인 문제를 등록한다.
     * When: 문제를 승인한다.
     * Then: 문제를 조회하면 숨김 상태이다.
     */
    @Test
    void approveQuiz() {
        //given
        Map<String, Object> 문제_생성_요청값 = 문제_생성_요청값(1L, "인덱스", "+++은 기본 인덱스이다.", List.of("pk", "기본키", "기본 키"));
        문제_생성_요청값.put("status", "UNAPPROVED");
        long quizId = 문제_생성(accessToken, 문제_생성_요청값).jsonPath().getLong("id");

        //when
        문제_상태_변경(accessToken, quizId, "PRIVATE");

        //then
        assertThat(문제_상세_조회(accessToken, quizId).jsonPath().getString("status")).isEqualTo("PRIVATE");
    }

    /**
     * Given: 미승인 상태인 문제를 등록한다.
     * When: 문제를 반려한다.
     * Then: 문제를 조회하면 반려 상태이다.
     */
    @Test
    void rejectQuiz() {
        //given
        Map<String, Object> 문제_생성_요청값 = 문제_생성_요청값(1L, "인덱스", "+++은 기본 인덱스이다.", List.of("pk", "기본키", "기본 키"));
        문제_생성_요청값.put("status", "UNAPPROVED");
        long quizId = 문제_생성(accessToken, 문제_생성_요청값).jsonPath().getLong("id");

        //when
        문제_상태_변경(accessToken, quizId, "REJECTED");

        //then
        assertThat(문제_상세_조회(accessToken, quizId).jsonPath().getString("status")).isEqualTo("REJECTED");
    }

    /**
     * Given: 공개 상태인 문제를 등록한다.
     * When: 문제를 숨긴다.
     * Then: 문제를 조회하면 숨김 상태이다.
     * When: 문제를 공개한다.
     * Then: 문제를 조회하면 공개 상태이다.
     */

}
