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

import static com.cstapin.auth.dto.MemberRequest.*;
import static com.cstapin.auth.dto.MemberResponse.LoginResponse;
import static com.cstapin.auth.dto.MemberResponse.TokenResponse;

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

    @PostMapping("/auth/reissue")
    public ResponseEntity<TokenResponse> reissueToken(@Valid @RequestBody ReissueTokenRequest requestBody) {

        TokenResponse response = authService.reissueToken(requestBody);
        return ResponseEntity.ok().body(response);
    }
}