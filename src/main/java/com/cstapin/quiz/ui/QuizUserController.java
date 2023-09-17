package com.cstapin.quiz.ui;

import com.cstapin.auth.domain.UserPrincipal;
import com.cstapin.quiz.service.QuizUserService;
import com.cstapin.quiz.service.dto.QuizRequest;
import com.cstapin.quiz.service.dto.QuizResponse;
import com.cstapin.quiz.service.dto.QuizzesResponse;
import com.cstapin.support.service.dto.PageRequest;
import com.cstapin.support.service.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/api/v1/user/quizzes")
@RequiredArgsConstructor
public class QuizUserController {
    private final QuizUserService quizUserService;

    @PostMapping
    public ResponseEntity<QuizResponse> createQuiz(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                   @Valid @RequestBody QuizRequest request) {
        QuizResponse response = quizUserService.createQuiz(request, userPrincipal.getUsername());

        return ResponseEntity.created(URI.create("/api/v1/user/quizzes/" + response.getId())).body(response);
    }

    @GetMapping("/my/making")
    public ResponseEntity<PageResponse<QuizzesResponse>> findQuizzesByAuthor(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                                             PageRequest pageRequest) {

        Page<QuizzesResponse> quizzes = quizUserService.findQuizzesByAuthor(userPrincipal.getUsername(), pageRequest.getPageable());

        return ResponseEntity.ok().body(new PageResponse<>(quizzes));
    }

}
