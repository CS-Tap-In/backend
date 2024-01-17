package com.cstapin.auth.ui;

import com.cstapin.auth.oauth2.github.GithubCodeRequest;
import com.cstapin.auth.service.AuthService;
import com.cstapin.auth.service.dto.*;
import com.cstapin.member.domain.MemberRole;
import com.cstapin.member.persistence.MemberEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {

        LoginResponse response = authService.login(request);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/login/github")
    public ResponseEntity<LoginResponse> loginFromGithub(@Valid @RequestBody GithubCodeRequest request) {
        LoginResponse response = authService.loginFromGithub(request);

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/join/admin")
    public ResponseEntity<Void> joinAdmin(@Valid @RequestBody JoinRequest request) {

        authService.join(request, MemberRole.ADMIN);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenResponse> reissueToken(@Valid @RequestBody ReissueTokenRequest requestBody) {

        TokenResponse response = authService.reissueToken(requestBody);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/join/user")
    public ResponseEntity<Void> joinUser(@Valid @RequestBody JoinRequest request) {

        authService.join(request, MemberRole.USER);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/health-check")
    public ResponseEntity<Void> checkIsHealth() {
        return ResponseEntity.ok().build();
    }

}