package com.cstapin.quiz.service;

import com.cstapin.quiz.domain.QuizParticipants;
import com.cstapin.quiz.service.dto.QuizParticipantsListRequest;
import com.cstapin.quiz.service.dto.QuizParticipantsResponse;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.Optional;

public interface QuizParticipantsRepository {

    Optional<QuizParticipants> findByPhoneNumberAndSameYearMonth(String phoneNumber, LocalDate date);

    QuizParticipants save(QuizParticipants quizParticipants);

    Page<QuizParticipants> getQuizParticipants(QuizParticipantsListRequest request);
}
