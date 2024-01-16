package com.cstapin.quiz.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Optional;

public interface QuizParticipantsRepositoryCustom {
    Optional<QuizParticipantsEntity> findByPhoneNumberAndSameYearMonth(String phoneNumber, LocalDate date);

    Page<QuizParticipantsEntity> findByYearMonth(YearMonth yearMonth, Pageable pageable);
}
