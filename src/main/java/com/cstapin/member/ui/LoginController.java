package com.cstapin.member.ui;

import com.cstapin.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.cstapin.member.dto.UserRequest.LoginRequest;
import static com.cstapin.member.dto.UserResponse.LoginResponse;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final MemberService memberService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {

        LoginResponse response = memberService.login(request);
        return ResponseEntity.ok().body(response);
    }
}