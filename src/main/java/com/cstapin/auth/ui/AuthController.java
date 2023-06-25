package com.cstapin.auth.ui;

import com.cstapin.auth.service.AuthService;
import com.cstapin.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.cstapin.member.dto.MemberRequest.JoinRequest;
import static com.cstapin.member.dto.MemberRequest.LoginRequest;
import static com.cstapin.member.dto.MemberResponse.LoginResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {

        LoginResponse response = authService.login(request);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/join/admin")
    public ResponseEntity<Void> join(@Valid @RequestBody JoinRequest request) {

        authService.join(request, Member.MemberRole.ADMIN);
        return ResponseEntity.ok().body(null);
    }
}