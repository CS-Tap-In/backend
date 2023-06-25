package com.cstapin.auth.ui;

import com.cstapin.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.cstapin.member.dto.MemberRequest.LoginRequest;
import static com.cstapin.member.dto.MemberResponse.LoginResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {

        LoginResponse response = memberService.login(request);
        return ResponseEntity.ok().body(response);
    }
}