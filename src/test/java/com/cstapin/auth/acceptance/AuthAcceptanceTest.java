package com.cstapin.auth.acceptance;

import com.cstapin.utils.AcceptanceTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

import static com.cstapin.auth.acceptance.AuthSteps.관리자_회원가입_요청;
import static com.cstapin.auth.acceptance.AuthSteps.로그인_요청;
import static org.assertj.core.api.Assertions.assertThat;

public class AuthAcceptanceTest extends AcceptanceTest {

    private static final String USERNAME = "user";
    private static final String PASSWORD = "password123@";
    private static final String NICKNAME = "nickname";
    @Value("${props.join.admin}")
    private String joinAdminSecretKey;
    private ExtractableResponse<Response> 관리자_회원가입_응답;

    @BeforeEach
    void setupFixture() {
        관리자_회원가입_응답 = 관리자_회원가입_요청(USERNAME, PASSWORD, NICKNAME, joinAdminSecretKey);
    }

    @Test
    @DisplayName("관리자 회원가입을 한다.")
    void join() {
        //then
        assertThat(관리자_회원가입_응답.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * Given: 잘못된 secret key 를 입력하고 관리자 회원가입을 한다.
     * Then: 예외를 발생한다.
     */
    @Test
    @DisplayName("잘못된 secret key로 회원가입을 하는 경우")
    void joinAdminUserWithIllegalSecretKey() {
        //given
        var response = 관리자_회원가입_요청(USERNAME, PASSWORD, NICKNAME, "illegal" + joinAdminSecretKey);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given: 관리자 회원가입을 한다.
     * When: 이 전과 같은 username 으로 관리자 회원가입을 한다.
     * Then: 예외를 발생한다.
     */
    @Test
    @DisplayName("중복되는 username 으로 회원가입을 하는 경우")
    void joinAdminUserWithDuplicateUsername() {
        //when
        var responseWithDuplicateUsername =
                관리자_회원가입_요청(USERNAME, PASSWORD, NICKNAME, joinAdminSecretKey);

        //then
        assertThat(responseWithDuplicateUsername.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given: 관리자 회원가입을 한다.
     * Then: 로그인을 하면 토큰이 반환된다.
     */
    @Test
    void login() {
        //then
        var loginResponse = 로그인_요청(USERNAME, PASSWORD);
        assertThat(loginResponse.jsonPath().getString("accessToken")).isNotEmpty();
    }

    /**
     * Given: 관리자 회원가입을 한다.
     * When: 다른 아이디로 로그인 요청을 한다.
     * Then: 예외를 발생한다.
     */
    @Test
    void loginWithIllegalUsername() {
        //when
        var response = 로그인_요청("illegal" + USERNAME, PASSWORD);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given: 관리자 회원가입을 한다.
     * When: 다른 비밀번호로 로그인 요청을 한다.
     * Then: 예외를 발생한다.
     */
    @Test
    void loginWithIllegalPassword() {
        //when
        var response = 로그인_요청(USERNAME, "illegal" + PASSWORD);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
