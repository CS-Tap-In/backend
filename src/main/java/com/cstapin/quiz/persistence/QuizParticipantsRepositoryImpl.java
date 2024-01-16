package com.cstapin.quiz.persistence;

import com.cstapin.quiz.domain.QuizParticipants;
import com.cstapin.quiz.service.QuizParticipantsRepository;
import com.cstapin.quiz.service.dto.QuizParticipantsListRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class QuizParticipantsRepositoryImpl implements QuizParticipantsRepository {

    private final QuizParticipantsJpaRepository quizParticipantsJpaRepository;

    @Override
    public Optional<QuizParticipants> findByPhoneNumberAndSameYearMonth(String phoneNumber, LocalDate date) {
        return quizParticipantsJpaRepository.findByPhoneNumberAndSameYearMonth(phoneNumber, date)
                .map(QuizParticipants::from);
    }

    @Override
    public QuizParticipants save(QuizParticipants quizParticipants) {
        QuizParticipantsEntity quizParticipantsEntity = quizParticipantsJpaRepository
                .save(quizParticipants.toQuizParticipantsEntity());

        return QuizParticipants.from(quizParticipantsEntity);
    }

    @Override
    public Page<QuizParticipants> getQuizParticipants(QuizParticipantsListRequest request) {
        return quizParticipantsJpaRepository.findByYearMonth(request.getYm(), request.getPageable())
                .map(QuizParticipants::from);
    }
}
