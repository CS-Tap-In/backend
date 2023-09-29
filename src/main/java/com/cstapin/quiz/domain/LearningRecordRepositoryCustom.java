package com.cstapin.quiz.domain;

import com.cstapin.quiz.service.dto.DailyQuizzesResponse;
import com.cstapin.quiz.service.dto.QuizCountByCategoryId;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface LearningRecordRepositoryCustom {
    List<LearningRecord> findLatestLearningRecords(Long memberId, int size);

    List<Quiz> findUnSolvedQuiz(Long memberId);

    List<DailyQuizzesResponse> findByMemberIdAndLocalDate(Long memberId, LocalDate date);

    List<QuizCountByCategoryId> findStudyQuizCountByQuizCategory(Long memberId);
}
