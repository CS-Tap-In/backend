package com.cstapin.quiz.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuizCategoryRepository extends JpaRepository<QuizCategory, Long> {

    @Override
    @Query("select qc from QuizCategory qc where qc.status <> 'DELETED'")
    List<QuizCategory> findAll();
}
