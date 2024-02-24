package com.cstapin.auth.ui;

import com.cstapin.auth.service.AuthService;
import com.cstapin.auth.service.dto.WebTokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/web/auth")
@RequiredArgsConstructor
public class AuthWebController {

    private final AuthService authService;

    @PostMapping("/token")
    public ResponseEntity<WebTokenResponse> issueWebToken() {

        WebTokenResponse response = authService.issueWebToken();
        return ResponseEntity.ok().body(response);
    }
}
