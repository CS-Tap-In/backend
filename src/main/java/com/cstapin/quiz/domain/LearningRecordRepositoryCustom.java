package com.cstapin.quiz.domain;

import java.util.List;

public interface LearningRecordRepositoryCustom {
    List<LearningRecord> findLatestLearningRecords(Long memberId, int size);

    List<Quiz> findUnSolvedQuiz(Long memberId);
}
