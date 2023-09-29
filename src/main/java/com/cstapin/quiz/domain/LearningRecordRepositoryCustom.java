package com.cstapin.quiz.domain;

import com.cstapin.quiz.service.dto.DailyQuizzesResponse;

import java.time.LocalDate;
import java.util.List;

public interface LearningRecordRepositoryCustom {
    List<LearningRecord> findLatestLearningRecords(Long memberId, int size);

    List<Quiz> findUnSolvedQuiz(Long memberId);

    List<DailyQuizzesResponse> findByMemberIdAndLocalDate(Long memberId, LocalDate date);
}
