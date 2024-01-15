package com.cstapin.quiz.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizParticipantsJpaRepository extends JpaRepository<QuizParticipantsEntity, Long>,
        QuizParticipantsRepositoryCustom {
}
