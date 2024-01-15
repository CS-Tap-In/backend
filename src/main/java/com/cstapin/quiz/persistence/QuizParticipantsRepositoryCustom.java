package com.cstapin.quiz.persistence;

import java.time.LocalDate;
import java.util.Optional;

public interface QuizParticipantsRepositoryCustom {
    Optional<QuizParticipantsEntity> findByPhoneNumberAndSameYearMonth(String phoneNumber, LocalDate date);
}
