package com.cstapin.quiz.service;

import com.cstapin.member.domain.Member;
import com.cstapin.member.service.query.MemberQueryService;
import com.cstapin.quiz.domain.*;
import com.cstapin.quiz.service.dto.DailyQuizzesSummaryResponse;
import com.cstapin.quiz.service.dto.QuizRequest;
import com.cstapin.quiz.service.dto.QuizResponse;
import com.cstapin.quiz.service.dto.QuizzesResponse;
import com.cstapin.quiz.service.query.QuizCategoryQueryService;
import com.cstapin.quiz.service.query.QuizQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuizUserService {

    private final LearningRecordRepository learningRecordRepository;
    private final DailyQuizSelector dailyQuizSelector;
    private final QuizQueryService quizQueryService;
    private final QuizRepository quizRepository;
    private final QuizCategoryQueryService quizCategoryQueryService;
    private final MemberQueryService memberQueryService;

    @Transactional
    public QuizResponse createQuiz(QuizRequest request, String username) {
        Member author = memberQueryService.findByUsername(username);
        Quiz quiz = quizRepository.save(request.toQuiz(author, quizCategoryQueryService.findById(request.getCategoryId())));
        return QuizResponse.from(quiz);
    }

    public Page<QuizzesResponse> findQuizzesByAuthor(String username, Pageable pageable) {
        Member author = memberQueryService.findByUsername(username);
        return quizQueryService.findQuizzesByAuthor(author.getId(), pageable);
    }

    @Transactional
    public DailyQuizzesSummaryResponse selectDailyQuizzes(String username) {
        Member member = memberQueryService.findByUsername(username);
        DailySelectedQuizzes dailySelectedQuizzes = dailyQuizSelector.select(member.getId(), member.getDailyGoal());

        List<LearningRecord> learningRecords = dailySelectedQuizzes.getTotalQuizzes().stream()
                .map(quiz -> LearningRecord.of(member.getId(), quiz)).collect(Collectors.toList());

        learningRecordRepository.saveAll(learningRecords);

        return DailyQuizzesSummaryResponse.from(dailySelectedQuizzes);
    }
}
