package com.cstapin.quiz.service.query;

import com.cstapin.exception.notfound.LearningRecordNotFoundException;
import com.cstapin.quiz.domain.LearningRecord;
import com.cstapin.quiz.domain.LearningRecordRepository;
import com.cstapin.quiz.domain.LearningStatus;
import com.cstapin.quiz.domain.Quiz;
import com.cstapin.quiz.service.dto.QuizCountByCategoryId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LearningRecordQueryService {

    private final LearningRecordRepository learningRecordRepository;

    public List<LearningRecord> findLatestLearningRecords(Long memberId, int size) {
        return learningRecordRepository.findLatestLearningRecords(memberId, size, LocalDate.now().atStartOfDay());
    }

    public List<Quiz> findUnSolvedQuiz(Long memberId) {
        return learningRecordRepository.findUnSolvedQuiz(memberId);
    }

    public LearningRecord findById(Long learningRecordId) {
        return learningRecordRepository.findById(learningRecordId).orElseThrow(LearningRecordNotFoundException::new);
    }

    public List<LearningRecord> findLearningRecords(Long memberId, LocalDate date) {
        return learningRecordRepository.findLearningRecords(memberId, date);
    }

    public List<QuizCountByCategoryId> findLearningRecords(Long memberId) {
        List<QuizCountByCategoryId> quizCountByQuizCategory = learningRecordRepository.findStudyQuizCountByQuizCategory(memberId);

        Map<Long, String> quizCategoriesMap = quizCountByQuizCategory.stream()
                .collect(Collectors.toMap(QuizCountByCategoryId::getQuizCategoryId, QuizCountByCategoryId::getQuizCategoryTitle, (existing, replacement) -> existing));

        Map<Long, Long> quizCountMap = quizCountByQuizCategory.stream()
                .collect(Collectors.groupingBy(QuizCountByCategoryId::getQuizCategoryId, Collectors.summingLong(QuizCountByCategoryId::getCount)));

        return quizCountMap.entrySet().stream()
                .map(entry -> new QuizCountByCategoryId(quizCategoriesMap.get(entry.getKey()), entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    public int findCompleteQuizCount(Long memberId, LocalDate date) {
        return learningRecordRepository.findCompleteQuiz(memberId, date).size();
    }

    public boolean isDailyLearningComplete(Long memberId, LocalDate date) {
        List<LearningRecord> learningRecords = learningRecordRepository.findLearningRecords(memberId, date);

        boolean isDailyLearningComplete = learningRecords.stream()
                .allMatch(lr ->
                        LearningStatus.SUCCESS.equals(lr.getStatus()) || LearningStatus.RECOVERY.equals(lr.getStatus())
                );

        return learningRecords.size() != 0 && isDailyLearningComplete;
    }
}
