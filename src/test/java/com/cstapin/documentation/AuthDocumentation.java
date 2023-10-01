package com.cstapin.documentation;

import com.cstapin.auth.acceptance.AuthSteps;
import com.cstapin.auth.service.AuthService;
import com.cstapin.auth.service.dto.LoginResponse;
import com.cstapin.member.domain.Member;
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
        LoginResponse response = new LoginResponse("adf132abadf023fdfa", "adfaklgadjgadi", Member.MemberRole.ADMIN);

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
        LoginResponse response = new LoginResponse("adf132abadf023fdfa", "adfaklgadjgadi", Member.MemberRole.USER);

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

}
