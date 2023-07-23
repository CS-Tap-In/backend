package com.cstapin.auth.ui;

import com.cstapin.auth.service.AuthService;
import com.cstapin.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.cstapin.auth.service.dto.MemberRequest.*;
import static com.cstapin.auth.service.dto.MemberResponse.LoginResponse;
import static com.cstapin.auth.service.dto.MemberResponse.TokenResponse;

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

    @PostMapping("/join/admin")
    public ResponseEntity<Void> join(@Valid @RequestBody JoinRequest request) {

        authService.join(request, Member.MemberRole.ADMIN);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenResponse> reissueToken(@Valid @RequestBody ReissueTokenRequest requestBody) {

        TokenResponse response = authService.reissueToken(requestBody);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/api/v1/admin/test")
    public ResponseEntity<TokenResponse> test() {

        return null;
    }
}