package com.cstapin.quiz.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Long>, QuizRepositoryCustom {

    @Query("select q from Quiz q where q.id in :quizIds")
    List<Quiz> findByIdIn(@Param(value = "quizIds") List<Long> quizIds);

    @EntityGraph(attributePaths = {"quizCategory"})
    Page<Quiz> findByAuthorId(Long authorId, Pageable pageable);
}
