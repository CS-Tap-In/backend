package com.cstapin.quiz.service;

import com.cstapin.quiz.domain.QuizParticipants;
import com.cstapin.quiz.service.dto.QuizParticipantsListRequest;
import org.springframework.data.domain.Page;

import java.time.YearMonth;
import java.util.Optional;

public interface QuizParticipantsRepository {

    Optional<QuizParticipants> findByPhoneNumberAndSameYearMonth(String phoneNumber, YearMonth yearMonth);

    QuizParticipants save(QuizParticipants quizParticipants);

    Page<QuizParticipants> getQuizParticipants(QuizParticipantsListRequest request);
}
