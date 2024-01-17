package com.cstapin.quiz.persistence;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import static com.cstapin.quiz.persistence.QQuizParticipantsEntity.quizParticipantsEntity;

@RequiredArgsConstructor
public class QuizParticipantsRepositoryCustomImpl implements QuizParticipantsRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<QuizParticipantsEntity> findByPhoneNumberAndSameYearMonth(String phoneNumber, YearMonth yearMonth) {
        QuizParticipantsEntity entity = queryFactory.selectFrom(quizParticipantsEntity)
                .where(
                        quizParticipantsEntity.phoneNumber.eq(phoneNumber),
                        quizParticipantsEntity.createdAt.month().eq(yearMonth.getMonthValue()),
                        quizParticipantsEntity.createdAt.year().eq(yearMonth.getYear())
                )
                .fetchFirst();

        return Optional.ofNullable(entity);
    }

    @Override
    public Page<QuizParticipantsEntity> findByYearMonth(YearMonth yearMonth, Pageable pageable) {
        List<QuizParticipantsEntity> content = queryFactory.selectFrom(quizParticipantsEntity)
                .where(
                        quizParticipantsEntity.createdAt.month().eq(yearMonth.getMonthValue()),
                        quizParticipantsEntity.createdAt.year().eq(yearMonth.getYear())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(
                        quizParticipantsEntity.correctCount.desc(),
                        quizParticipantsEntity.updatedAt.asc()
                )
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(quizParticipantsEntity.count())
                .from(quizParticipantsEntity)
                .where(
                        quizParticipantsEntity.createdAt.month().eq(yearMonth.getMonthValue()),
                        quizParticipantsEntity.createdAt.year().eq(yearMonth.getYear())
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }
}
