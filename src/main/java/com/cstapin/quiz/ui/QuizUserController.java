package com.cstapin.quiz.ui;

import com.cstapin.auth.domain.UserPrincipal;
import com.cstapin.quiz.service.QuizUserService;
import com.cstapin.quiz.service.dto.*;
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
import java.util.List;

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

    @PostMapping("/daily")
    public ResponseEntity<DailyQuizzesSummaryResponse> selectDailyQuizzes(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        DailyQuizzesSummaryResponse response = quizUserService.selectDailyQuizzes(userPrincipal.getUsername());

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/daily")
    public ResponseEntity<List<DailyQuizzesResponse>> findDailyQuizzes(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<DailyQuizzesResponse> response = quizUserService.findDailyQuizzes(userPrincipal.getUsername());

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/daily/learning-records/{learningRecordId}")
    public ResponseEntity<Void> updateLearningRecordStatus(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                           @PathVariable Long learningRecordId,
                                                           @Valid @RequestBody LearningRecordStatusRequest request) {
        quizUserService.updateLearningRecordStatus(userPrincipal.getUsername(), learningRecordId, request);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/learning-records")
    public ResponseEntity<List<LearningRecordsResponse>> findLearningRecords(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<LearningRecordsResponse> response = quizUserService.findLearningRecords(userPrincipal.getUsername());

        return ResponseEntity.ok().body(response);
    }

}
