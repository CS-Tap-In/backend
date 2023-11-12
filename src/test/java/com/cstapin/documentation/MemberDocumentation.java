package com.cstapin.documentation;

import com.cstapin.member.service.MemberService;
import com.cstapin.member.service.dto.MembersResponse;
import com.cstapin.member.service.dto.ProfileResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;

import static com.cstapin.auth.acceptance.AuthSteps.로그인_요청;
import static com.cstapin.member.acceptance.MemberSteps.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class MemberDocumentation extends Documentation {

    @MockBean
    private MemberService memberService;

    private String userAccessToken;
    private String adminAccessToken;

    @BeforeEach
    void setUp() {
        ExtractableResponse<Response> 로그인_응답 = 로그인_요청("user", "password123@");
        userAccessToken = 로그인_응답.jsonPath().getString("accessToken");

        ExtractableResponse<Response> 관리자_로그인_응답 = 로그인_요청("admin", "password123@");
        adminAccessToken = 관리자_로그인_응답.jsonPath().getString("accessToken");
    }

    @Test
    void findProfile() {
        //given
        ProfileResponse response = new ProfileResponse(1L, "user", "http://avatar.com/1", 0, 10, false);

        //when
        when(memberService.findProfile(anyString())).thenReturn(response);

        //then
        프로필_조회(getRequestSpecification("user-find-profile").auth().oauth2(userAccessToken));
    }

    @Test
    void changeDailyGoal() {
        //then
        하루_퀴즈_목표치_변경(getRequestSpecification("user-change-dailyGoal").auth().oauth2(userAccessToken), 20);
    }

    @Test
    void findMembers() {
        //given
        MembersResponse response = new MembersResponse(1L, "youkihoon@minjilove.com", "민지팬클럽회장", LocalDateTime.now());

        //when
        when(memberService.findMembers(any())).thenReturn(new PageImpl<>(List.of(response), PageRequest.of(0, 10), 1L));

        //then
        회원_목록_조회(getRequestSpecification("admin-find-members").auth().oauth2(adminAccessToken), "youkihoon");
    }

    @Test
    void withdrawMember() {
        //then
        회원탈퇴_요청(getRequestSpecification("user-withdrawal").auth().oauth2(userAccessToken));
    }
}
