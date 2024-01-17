package com.cstapin.quiz.ui;


import com.cstapin.quiz.service.QuizUserService;
import com.cstapin.quiz.service.dto.*;
import com.cstapin.support.service.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.YearMonth;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/web/user/quizzes")
@RequiredArgsConstructor
public class QuizUserWebController {

    private final QuizUserService quizUserService;

    @PostMapping("/random")
    public ResponseEntity<List<RandomQuizzesResponse>> getRandomQuizzes(@Valid @RequestBody RandomQuizzesRequest request) {
        List<RandomQuizzesResponse> randomQuizzes = quizUserService.getRandomQuizzes(request);

        return ResponseEntity.ok().body(randomQuizzes);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<QuizCategoryResponse>> getQuizCategories() {
        List<QuizCategoryResponse> quizCategoryResponses = quizUserService.findQuizCategories();

        return ResponseEntity.ok().body(quizCategoryResponses);
    }

    @PostMapping("/random/results")
    public ResponseEntity<QuizParticipantsResponse> saveOrUpdateQuizParticipants(@Valid @RequestBody QuizParticipantsRequest request) {
        QuizParticipantsResponse response = quizUserService.saveOrUpdateQuizParticipants(request, YearMonth.now());

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/random/results")
    public ResponseEntity<PageResponse<QuizParticipantsResponse>> getQuizParticipants(@Valid QuizParticipantsListRequest request) {

        Page<QuizParticipantsResponse> quizCategoryResponses = quizUserService.getQuizParticipants(request);

        return ResponseEntity.ok().body(new PageResponse<>(quizCategoryResponses));
    }
}
