package com.cstapin.quiz.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.cstapin.quiz.domain.QLearningRecord.learningRecord;
import static com.cstapin.quiz.domain.QQuiz.quiz;

@RequiredArgsConstructor
public class LearningRecordRepositoryCustomImpl implements LearningRecordRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<LearningRecord> findLatestLearningRecords(Long memberId, int size) {
        return queryFactory
                .selectFrom(learningRecord)
                .where(learningRecord.memberId.eq(memberId))
                .orderBy(learningRecord.createdAt.desc())
                .limit(size)
                .fetch();
    }

    @Override
    public List<Quiz> findUnSolvedQuiz(Long memberId) {
        List<Long> solvedQuizIds = queryFactory
                .select(learningRecord.quiz.id)
                .from(learningRecord)
                .where(
                        learningRecord.memberId.eq(memberId),
                        learningRecord.status.ne(LearningStatus.NONE)
                )
                .distinct()
                .fetch();

        return queryFactory
                .selectFrom(quiz)
                .where(
                        quiz.id.notIn(solvedQuizIds),
                        quiz.status.eq(QuizStatus.PUBLIC)
                )
                .fetch();
    }
}
