package com.cstapin.quiz.persistence;

import com.cstapin.quiz.domain.QuizParticipants;
import com.cstapin.quiz.service.QuizParticipantsRepository;
import com.cstapin.quiz.service.dto.QuizParticipantsListRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class QuizParticipantsRepositoryImpl implements QuizParticipantsRepository {

    private final QuizParticipantsJpaRepository quizParticipantsJpaRepository;

    @Override
    public Optional<QuizParticipants> findByPhoneNumberAndSameYearMonth(String phoneNumber, YearMonth yearMonth) {
        return quizParticipantsJpaRepository.findByPhoneNumberAndSameYearMonth(phoneNumber, yearMonth)
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

    @Override
    public long getRank(Long quizParticipantsId, YearMonth yearMonth) {
        int page = 0;
        int size = 50;
        int rank = 0;

        while (true) {
            Page<QuizParticipantsEntity> quizParticipantsPage =
                    quizParticipantsJpaRepository.findByYearMonth(yearMonth, PageRequest.of(page, size));

            List<QuizParticipantsEntity> content = quizParticipantsPage.getContent();

            for (QuizParticipantsEntity entity : content) {
                rank++;
                if (entity.matchId(quizParticipantsId)) {
                    return rank;
                }
            }

            if (!quizParticipantsPage.hasNext()) {
                break;
            }

            page++;
        }

        return 0; // 찾지 못한 경우
    }
}
