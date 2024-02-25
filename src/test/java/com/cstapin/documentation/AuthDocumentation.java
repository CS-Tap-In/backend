package com.cstapin.documentation;

import com.cstapin.auth.acceptance.AuthSteps;
import com.cstapin.auth.service.AuthService;
import com.cstapin.auth.service.dto.LoginResponse;
import com.cstapin.auth.service.dto.TokenResponse;
import com.cstapin.auth.service.dto.WebTokenResponse;
import com.cstapin.member.domain.MemberRole;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import static com.cstapin.auth.acceptance.AuthSteps.로그인_요청;
import static com.cstapin.auth.acceptance.AuthSteps.로그인_요청_깃허브;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AuthDocumentation extends Documentation {

    private static final String USERNAME = "youkihoon";
    private static final String PASSWORD = "password123@";

    @MockBean
    private AuthService authService;

    @Test
    void login() {
        //given
        LoginResponse response = new LoginResponse("adf132abadf023fdfa", "adfaklgadjgadi", MemberRole.ADMIN);

        //when
        when(authService.login(any())).thenReturn(response);

        //then
        로그인_요청(getRequestSpecification("auth-login"), USERNAME, PASSWORD);
    }

    @Test
    void joinAdmin() {
        //then
        AuthSteps.관리자_회원가입_요청(getRequestSpecification("join-admin"), USERNAME, PASSWORD, "62hoon99", "secret-key");
    }

    @Test
    void loginFromGithub() {
        //given
        LoginResponse response = new LoginResponse("adf132abadf023fdfa", "adfaklgadjgadi", MemberRole.USER);

        //when
        when(authService.loginFromGithub(any())).thenReturn(response);

        //then
        로그인_요청_깃허브(getRequestSpecification("auth-login-github"), "cda12gk31");
    }

    @Test
    void joinUser() {
        //then
        AuthSteps.일반_회원가입_요청(getRequestSpecification("join-normal-user"), USERNAME, PASSWORD, "62hoon99");
    }

    @Test
    void reissueToken() {
        //given
        TokenResponse response = new TokenResponse("dafds123fadsfhj", "adfadshuadfo");

        //when
        when(authService.reissueToken(any())).thenReturn(response);

        //then
        AuthSteps.토큰_재발급_요청(getRequestSpecification("auth-reissue-token"), "fadsfadsfasd");
    }

    @Test
    void issueWebToken(){
        //given
        WebTokenResponse response = new WebTokenResponse("asdfasd-123fafdsa-jhltyrutyj-hgfdb");

        //when
        when(authService.issueWebToken()).thenReturn(response);

        //then
        AuthSteps.웹토큰_발행(getRequestSpecification("auth-issue-web-token"));
    }
}
