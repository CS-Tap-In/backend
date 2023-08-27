package com.cstapin.quiz.ui;

import com.cstapin.auth.domain.UserPrincipal;
import com.cstapin.quiz.service.QuizAdminService;
import com.cstapin.quiz.service.dto.*;
import com.cstapin.support.service.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

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

    @GetMapping
    public ResponseEntity<PageResponse<QuizzesResponse>> findQuizzes(@Valid QuizRequestParams requestParams) {
        Page<QuizzesResponse> quizzes = quizAdminService.findQuizzes(requestParams);

        return ResponseEntity.ok().body(new PageResponse<>(quizzes));
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuizResponse> findQuizzes(@PathVariable(value = "id") Long quizId) {
        QuizResponse quiz = quizAdminService.findQuiz(quizId);

        return ResponseEntity.ok().body(quiz);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<QuizCategoryResponse>> findQuizzes() {
        List<QuizCategoryResponse> quizCategories = quizAdminService.findQuizCategory();

        return ResponseEntity.ok().body(quizCategories);
    }

}
