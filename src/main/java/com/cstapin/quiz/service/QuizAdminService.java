package com.cstapin.quiz.service;

import com.cstapin.member.domain.Member;
import com.cstapin.member.service.query.MemberQueryService;
import com.cstapin.quiz.domain.Quiz;
import com.cstapin.quiz.domain.QuizCategory;
import com.cstapin.quiz.domain.QuizCategoryRepository;
import com.cstapin.quiz.domain.QuizRepository;
import com.cstapin.quiz.service.dto.*;
import com.cstapin.quiz.service.query.QuizCategoryQueryService;
import com.cstapin.quiz.service.query.QuizQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuizAdminService {

    private final QuizRepository quizRepository;
    private final QuizCategoryRepository quizCategoryRepository;
    private final QuizQueryService quizQueryService;
    private final MemberQueryService memberQueryService;
    private final QuizCategoryQueryService quizCategoryQueryService;

    @Transactional
    public QuizResponse createQuiz(QuizRequest request, String username) {
        Member author = memberQueryService.findByUsername(username);
        Quiz quiz = quizRepository.save(request.toQuiz(author, quizCategoryQueryService.findById(request.getCategoryId())));
        return QuizResponse.from(quiz);
    }

    public Page<QuizzesResponse> findQuizzes(QuizRequestParams requestParams) {
        return quizQueryService.findQuizzes(requestParams);
    }

    public QuizResponse findQuiz(Long quizId) {
        return QuizResponse.from(quizQueryService.findById(quizId));
    }

    public List<QuizCategoryResponse> findQuizCategory() {
        return quizCategoryRepository.findAll().stream()
                .map(quizCategory -> new QuizCategoryResponse(quizCategory.getTitle(), quizCategory.getStatus()))
                .collect(Collectors.toList());
    }

    @Transactional
    public QuizCategoryResponse createQuizCategory(QuizCategoryRequest request) {
        QuizCategory quizCategory = quizCategoryRepository.save(request.toQuizCategory());
        return new QuizCategoryResponse(quizCategory.getTitle(), quizCategory.getStatus());
    }
}
