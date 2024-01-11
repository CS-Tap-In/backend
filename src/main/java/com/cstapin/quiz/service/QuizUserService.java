package com.cstapin.quiz.service;

import com.cstapin.member.domain.Member;
import com.cstapin.member.service.query.MemberQueryService;
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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
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
    private final MemberQueryService memberQueryService;
    private final RandomQuizSelector randomQuizSelector;

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

        if (dailySelectedQuizzes.isFirstTimeQuestionToday()) {
            List<LearningRecord> learningRecords = dailySelectedQuizzes.getTotalQuizzes().stream()
                    .map(quiz -> LearningRecord.of(member.getId(), quiz)).collect(Collectors.toList());

            learningRecordRepository.saveAll(learningRecords);
        }

        return DailyQuizzesSummaryResponse.from(dailySelectedQuizzes);
    }

    public List<DailyQuizzesResponse> findDailyQuizzes(String username) {
        Member member = memberQueryService.findByUsername(username);
        return learningRecordRepository.findByMemberIdAndLocalDate(member.getId(), LocalDate.now())
                .stream().filter(res -> LearningStatus.FAILURE.equals(res.getLearningStatus()) || LearningStatus.NONE.equals(res.getLearningStatus()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateLearningRecordStatus(String username, Long learningRecordId, LearningRecordStatusRequest request) {
        Member member = memberQueryService.findByUsername(username);
        LearningRecord learningRecord = learningRecordQueryService.findById(learningRecordId);
        learningRecord.updateStatus(member.getId(), request.getLearningStatus());
    }

    public List<LearningRecordsResponse> findLearningRecords(String username) {
        Member member = memberQueryService.findByUsername(username);

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

    public List<RandomQuizzesResponse> getRandomQuizzes(RandomQuizzesRequest request) {
        return randomQuizSelector.select(request.getQuizCategoryIds(), (quizzes, count) -> {
            if (quizzes.isEmpty()) {
                return Collections.emptyList();
            }
                    return new Random().ints(0, quizzes.size())
                            .distinct()
                            .limit(Math.min(count, quizzes.size()))
                            .mapToObj(quizzes::get)
                            .collect(Collectors.toList());
                })
                .stream()
                .map(RandomQuizzesResponse::from)
                .collect(Collectors.toList());

    }
}
