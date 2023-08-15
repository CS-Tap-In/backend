package com.cstapin.quiz.ui;

import com.cstapin.auth.domain.UserPrincipal;
import com.cstapin.quiz.service.QuizAdminService;
import com.cstapin.quiz.service.dto.QuizRequest;
import com.cstapin.quiz.service.dto.QuizResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/api/v1/admin/quizzes")
@RequiredArgsConstructor
public class QuizAdminController {

    private final QuizAdminService quizAdminService;

    @PostMapping
    public ResponseEntity<QuizResponse> createQuiz(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                   @Valid @RequestBody QuizRequest request) {
        QuizResponse response = quizAdminService.createQuiz(request, userPrincipal.getUsername());

        return ResponseEntity.created(URI.create("/api/v1/admin/quizzes/" + response.getId())).body(response);
    }

}
