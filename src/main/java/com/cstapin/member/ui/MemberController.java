package com.cstapin.member.ui;


import com.cstapin.auth.domain.UserPrincipal;
import com.cstapin.member.service.MemberService;
import com.cstapin.member.service.dto.ProfileResponse;
import com.cstapin.quiz.service.dto.QuizRequestParams;
import com.cstapin.quiz.service.dto.QuizzesResponse;
import com.cstapin.support.service.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/v1/user/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/profiles")
    public ResponseEntity<ProfileResponse> findProfile(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        ProfileResponse response = memberService.findProfile(userPrincipal.getUsername());

        return ResponseEntity.ok().body(response);
    }
}
