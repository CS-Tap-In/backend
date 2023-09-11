package com.cstapin.member.acceptance;

import com.cstapin.auth.acceptance.AuthSteps;
import com.cstapin.utils.AcceptanceTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.cstapin.member.acceptance.MemberSteps.프로필_조회;

public class MemberAcceptanceTest extends AcceptanceTest {
    /**
     * Given: 유저 계정으로 로그인 한다.
     * When: 프로필을 조회한다.
     * Then: 정보가 조회된다.
     */
    @Test
    void findProfile() {
        //given
        ExtractableResponse<Response> 로그인_응답값 = AuthSteps.로그인_요청("user", "password123@");

        //when
        ExtractableResponse<Response> 프로필_응답값 = 프로필_조회(로그인_응답값.jsonPath().getString("accessToken"));

        //then
        Assertions.assertThat(프로필_응답값.jsonPath().getString("avatarUrl")).isEqualTo("http://avatar.com/1");
        Assertions.assertThat(프로필_응답값.jsonPath().getString("nickname")).isEqualTo("user");
    }
}
