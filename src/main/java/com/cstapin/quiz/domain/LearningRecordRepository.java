package com.cstapin.quiz.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LearningRecordRepository extends JpaRepository<LearningRecord, Long>, LearningRecordRepositoryCustom {
}
