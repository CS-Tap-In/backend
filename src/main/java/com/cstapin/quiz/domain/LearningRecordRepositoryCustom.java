package com.cstapin.quiz.domain;

import com.cstapin.quiz.service.dto.DailyQuizzesResponse;
import com.cstapin.quiz.service.dto.QuizCountByCategoryId;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface LearningRecordRepositoryCustom {
    List<LearningRecord> findLatestLearningRecords(Long memberId, int size, LocalDateTime time);

    List<Quiz> findUnSolvedQuiz(Long memberId);

    List<DailyQuizzesResponse> findByMemberIdAndLocalDate(Long memberId, LocalDate date);

    List<QuizCountByCategoryId> findStudyQuizCountByQuizCategory(Long memberId);

    List<LearningRecord> findLearningRecords(Long memberId, LocalDate time);

    List<Quiz> findCompleteQuiz(Long memberId, LocalDate time);
}
