package com.cstapin.member.acceptance;

import com.cstapin.auth.acceptance.AuthSteps;
import com.cstapin.utils.AcceptanceTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static com.cstapin.auth.acceptance.AuthSteps.로그인_요청;
import static com.cstapin.auth.acceptance.AuthSteps.일반_회원가입_요청;
import static com.cstapin.member.acceptance.MemberSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

public class MemberEntityAcceptanceTest extends AcceptanceTest {

    private String accessToken;

    @BeforeEach
    void setUpFixture() {
        ExtractableResponse<Response> 로그인_응답값 = AuthSteps.로그인_요청("user", "password123@");
        accessToken = 로그인_응답값.jsonPath().getString("accessToken");
    }

    /**
     * Given: 유저 계정으로 로그인 한다.
     * When: 프로필을 조회한다.
     * Then: 정보가 조회된다.
     */
    @Test
    void findProfile() {
        //when
        ExtractableResponse<Response> 프로필_응답값 = 프로필_조회(accessToken);

        //then
        assertThat(프로필_응답값.jsonPath().getString("avatarUrl")).isEqualTo("http://avatar.com/1");
        assertThat(프로필_응답값.jsonPath().getString("nickname")).isEqualTo("user");
    }

    /**
     * Given: 유저 계정으로 로그인 한다.
     * When: 퀴즈 하루 목표치를 20개로 변경한다.
     * Then: 프로필을 조회하면 하루 목표치가 20개이다.
     */
    @Test
    void changeDailyGoal() {
        //when
        하루_퀴즈_목표치_변경(accessToken, 20);

        //then
        ExtractableResponse<Response> 프로필_응답값 = 프로필_조회(accessToken);
        assertThat(프로필_응답값.jsonPath().getInt("dailyGoal")).isEqualTo(20);
    }

    /**
     * Given: 회원가입을 한다.
     * When: 탈퇴한다.
     * Then: 로그인하면 실패한다.
     */
    @Test
    void withdrawMember() {
        //when
        회원탈퇴_요청(로그인_요청("user", "password123@").jsonPath().getString("accessToken"));

        //then
        assertThat(로그인_요청("user", "password123@").statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
