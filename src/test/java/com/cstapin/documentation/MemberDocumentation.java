package com.cstapin.documentation;

import com.cstapin.member.acceptance.MemberSteps;
import com.cstapin.member.service.MemberService;
import com.cstapin.member.service.dto.ProfileResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static com.cstapin.auth.acceptance.AuthSteps.로그인_요청;

public class MemberDocumentation extends Documentation {

    @MockBean
    private MemberService memberService;

    private String userAccessToken;

    @BeforeEach
    void setUp() {
        ExtractableResponse<Response> 로그인_응답 = 로그인_요청("user", "password123@");
        userAccessToken = 로그인_응답.jsonPath().getString("accessToken");
    }

    @Test
    void findProfile() {
        //given
        ProfileResponse response = new ProfileResponse(1L, "user", "http://avatar.com/1", 0, 10);

        //when
        when(memberService.findProfile(anyString())).thenReturn(response);

        //then
        MemberSteps.프로필_조회(getRequestSpecification("user-find-profile").auth().oauth2(userAccessToken));
    }
}
