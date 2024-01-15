package com.cstapin.quiz.persistence;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Optional;

import static com.cstapin.quiz.persistence.QQuizParticipantsEntity.quizParticipantsEntity;

@RequiredArgsConstructor
public class QuizParticipantsRepositoryCustomImpl implements QuizParticipantsRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<QuizParticipantsEntity> findByPhoneNumberAndSameYearMonth(String phoneNumber, LocalDate date) {
        QuizParticipantsEntity entity = queryFactory.selectFrom(quizParticipantsEntity)
                .where(
                        quizParticipantsEntity.phoneNumber.eq(phoneNumber),
                        quizParticipantsEntity.createdAt.month().eq(date.getMonth().getValue()),
                        quizParticipantsEntity.createdAt.year().eq(date.getYear())
                )
                .fetchFirst();

        return Optional.ofNullable(entity);
    }
}
