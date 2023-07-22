package com.cstapin.auth.acceptance;

import com.cstapin.utils.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static com.cstapin.auth.acceptance.AuthSteps.*;
import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptance extends AcceptanceTest {

    public static final String USERNAME = "user";
    public static final String PASSWORD = "password123@";
    private static final String NICKNAME = "nickname";

    @Test
    @DisplayName("관리자 회원가입을 한다.")
    void join() {
        //when
        var response = 관리자_회원가입_요청(USERNAME, PASSWORD, NICKNAME);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * Given: 관리자 회원가입을 한다.
     * Then: 로그인을 하면 토큰이 반환된다.
     */
    @Test
    void login() {
        //given
        관리자_회원가입_요청(USERNAME, PASSWORD, NICKNAME);

        //then
        var loginResponse = 로그인_요청(USERNAME, PASSWORD);
        assertThat(loginResponse.jsonPath().getString("accessToken")).isNotEmpty();
    }
}
