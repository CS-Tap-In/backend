package com.cstapin.quiz.service;

import com.cstapin.member.domain.Member;
import com.cstapin.member.persistence.MemberEntity;
import com.cstapin.member.service.MemberRepository;
import com.cstapin.quiz.domain.*;
import com.cstapin.quiz.service.dto.*;
import com.cstapin.quiz.service.query.LearningRecordQueryService;
import com.cstapin.quiz.service.query.QuizCategoryQueryService;
import com.cstapin.quiz.service.query.QuizQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuizUserService {

    private final QuizCategoryRepository quizCategoryRepository;
    private final LearningRecordQueryService learningRecordQueryService;
    private final LearningRecordRepository learningRecordRepository;
    private final DailyQuizSelector dailyQuizSelector;
    private final QuizQueryService quizQueryService;
    private final QuizRepository quizRepository;
    private final QuizCategoryQueryService quizCategoryQueryService;
    private final MemberRepository memberRepository;

    @Transactional
    public QuizResponse createQuiz(QuizRequest request, String username) {
        Member author = memberRepository.getByUsername(username);
        Quiz quiz = quizRepository.save(request.toQuiz(author.toMemberEntity(), quizCategoryQueryService.findById(request.getCategoryId())));
        return QuizResponse.from(quiz);
    }

    public Page<QuizzesResponse> findQuizzesByAuthor(String username, Pageable pageable) {
        Member author = memberRepository.getByUsername(username);
        return quizQueryService.findQuizzesByAuthor(author.getId(), pageable);
    }

    @Transactional
    public DailyQuizzesSummaryResponse selectDailyQuizzes(String username) {
        Member member = memberRepository.getByUsername(username);
        DailySelectedQuizzes dailySelectedQuizzes = member.selectQuizzes(dailyQuizSelector);

        if (dailySelectedQuizzes.isFirstTimeQuestionToday()) {
            List<LearningRecord> learningRecords = dailySelectedQuizzes.getTotalQuizzes().stream()
                    .map(quiz -> LearningRecord.of(member.getId(), quiz)).collect(Collectors.toList());

            learningRecordRepository.saveAll(learningRecords);
        }

        return DailyQuizzesSummaryResponse.from(dailySelectedQuizzes);
    }

    public List<DailyQuizzesResponse> findDailyQuizzes(String username) {
        Member member = memberRepository.getByUsername(username);
        return learningRecordRepository.findByMemberIdAndLocalDate(member.getId(), LocalDate.now())
                .stream().filter(res -> LearningStatus.FAILURE.equals(res.getLearningStatus()) || LearningStatus.NONE.equals(res.getLearningStatus()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateLearningRecordStatus(String username, Long learningRecordId, LearningRecordStatusRequest request) {
        Member member = memberRepository.getByUsername(username);
        LearningRecord learningRecord = learningRecordQueryService.findById(learningRecordId);
        learningRecord.updateStatus(member.getId(), request.getLearningStatus());
    }

    public List<LearningRecordsResponse> findLearningRecords(String username) {
        Member member = memberRepository.getByUsername(username);

        Map<Long, Long> quizCountByQuizCategoryMap = learningRecordQueryService.findLearningRecords(member.getId())
                .stream().collect(Collectors.toMap(QuizCountByCategoryId::getQuizCategoryId, QuizCountByCategoryId::getCount));

        List<QuizCountByCategoryId> allQuizCountByQuizCategoryIds = quizRepository.findQuizCountByQuizCategory();

        return allQuizCountByQuizCategoryIds.stream().map(dto -> new LearningRecordsResponse(
                dto.getQuizCategoryTitle(),
                quizCountByQuizCategoryMap.getOrDefault(dto.getQuizCategoryId(), 0L),
                dto.getCount())).collect(Collectors.toList());
    }

    public List<QuizCategoryResponse> findQuizCategories() {
        return quizCategoryRepository.findAll().stream()
                .map(QuizCategoryResponse::from)
                .collect(Collectors.toList());
    }
}
