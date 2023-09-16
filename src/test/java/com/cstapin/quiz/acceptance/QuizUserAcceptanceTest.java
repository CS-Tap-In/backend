package com.cstapin.quiz.acceptance;

import com.cstapin.auth.acceptance.AuthSteps;
import com.cstapin.utils.AcceptanceTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static com.cstapin.quiz.acceptance.QuizSteps.*;
import static org.assertj.core.api.Assertions.*;

public class QuizUserAcceptanceTest extends AcceptanceTest {

    private String accessToken;
    private String adminAccessToken;

    @BeforeEach
    void setUpFixture() {
        //로그인
        ExtractableResponse<Response> 로그인 = AuthSteps.로그인_요청("user", "password123@");
        accessToken = 로그인.jsonPath().getString("accessToken");

        //관리자 로그인
        ExtractableResponse<Response> 관리자_로그인 = AuthSteps.로그인_요청("admin", "password123@");
        adminAccessToken = 관리자_로그인.jsonPath().getString("accessToken");

        //카테고리
        문제_카테고리_생성(adminAccessToken, 문제_카테고리_요청값("데이터베이스"));
    }

    /**
     * Given: 문제를 등록한다.
     * When: 내가 만든 문제들을 조회한다.
     * Then: 문제가 조회된다.
     * Then: 문제는 미승인(UNAPPROVED) 상태 이다.
     */
    @Test
    void createQuiz() {
        //given
        Map<String, Object> 문제_생성_요청값 = 유저가_문제_생성_요청값(1L, "인덱스", "+++은 기본 인덱스이다.", List.of("pk", "기본키", "기본 키"));
        유저가_문제_생성(accessToken, 문제_생성_요청값);

        //when
        ExtractableResponse<Response> 내가_만든_문제_목록_조회 = 내가_만든_문제_목록_조회(accessToken);

        //then
        assertThat(내가_만든_문제_목록_조회.jsonPath().getList("content.title")).containsExactly("인덱스");
        assertThat(내가_만든_문제_목록_조회.jsonPath().getList("content.status")).containsExactly("UNAPPROVED");
    }

}
