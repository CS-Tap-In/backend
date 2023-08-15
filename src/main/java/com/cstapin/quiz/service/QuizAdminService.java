package com.cstapin.quiz.service;

import com.cstapin.member.domain.Member;
import com.cstapin.member.service.query.MemberQueryService;
import com.cstapin.quiz.domain.Quiz;
import com.cstapin.quiz.domain.QuizRepository;
import com.cstapin.quiz.service.dto.QuizRequest;
import com.cstapin.quiz.service.dto.QuizRequestParams;
import com.cstapin.quiz.service.dto.QuizResponse;
import com.cstapin.quiz.service.query.QuizQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuizAdminService {

    private final QuizRepository quizRepository;
    private final QuizQueryService quizQueryService;
    private final MemberQueryService memberQueryService;

    @Transactional
    public QuizResponse createQuiz(QuizRequest request, String username) {
        Member author = memberQueryService.findByUsername(username);
        Quiz quiz = quizRepository.save(request.toQuiz(author));
        return QuizResponse.from(quiz);
    }

    public Page<QuizResponse> findQuizzes(QuizRequestParams requestParams) {
        return quizQueryService.findQuizzes(requestParams);
    }

    public QuizResponse findQuiz(Long quizId) {
        return QuizResponse.from(quizQueryService.findById(quizId));
    }
}
