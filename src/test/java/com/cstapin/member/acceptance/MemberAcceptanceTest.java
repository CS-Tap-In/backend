package com.cstapin.member.acceptance;

import com.cstapin.auth.acceptance.AuthSteps;
import com.cstapin.utils.AcceptanceTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.cstapin.member.acceptance.MemberSteps.프로필_조회;
import static com.cstapin.member.acceptance.MemberSteps.하루_퀴즈_목표치_변경;
import static org.assertj.core.api.Assertions.*;

public class MemberAcceptanceTest extends AcceptanceTest {

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
}
