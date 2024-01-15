package com.cstapin.quiz.service;

import com.cstapin.quiz.domain.QuizParticipants;

import java.time.LocalDate;
import java.util.Optional;

public interface QuizParticipantsRepository {

    Optional<QuizParticipants> findByPhoneNumberAndSameYearMonth(String phoneNumber, LocalDate date);

    QuizParticipants save(QuizParticipants quizParticipants);
}
