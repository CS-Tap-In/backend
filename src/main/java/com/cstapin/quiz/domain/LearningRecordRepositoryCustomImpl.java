package com.cstapin.quiz.domain;

import com.cstapin.quiz.service.dto.DailyQuizzesResponse;
import com.cstapin.quiz.service.dto.QDailyQuizzesResponse;
import com.cstapin.quiz.service.dto.QQuizCountByCategoryId;
import com.cstapin.quiz.service.dto.QuizCountByCategoryId;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
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

    @Override
    public List<DailyQuizzesResponse> findByMemberIdAndLocalDate(Long memberId, LocalDate date) {
        return queryFactory
                .select(new QDailyQuizzesResponse(
                        learningRecord.id,
                        learningRecord.status,
                        learningRecord.quiz.id,
                        learningRecord.quiz.quizCategory.title,
                        learningRecord.quiz.title,
                        learningRecord.quiz.problem,
                        learningRecord.quiz.answer
                ))
                .from(learningRecord)
                .where(
                        learningRecord.memberId.eq(memberId),
                        learningRecord.createdAt.after(date.atStartOfDay())
                )
                .fetch();
    }

    @Override
    public List<QuizCountByCategoryId> findStudyQuizCountByQuizCategory(Long memberId) {
        return queryFactory
                .select(new QQuizCountByCategoryId(
                        quiz.quizCategory.title,
                        quiz.quizCategory.id,
                        learningRecord.quiz.countDistinct()))
                .from(learningRecord)
                .where(
                        learningRecord.memberId.eq(memberId),
                        learningRecord.status.ne(LearningStatus.NONE)
                )
                .join(learningRecord.quiz, quiz)
                .join(quiz.quizCategory)
                .groupBy(quiz.quizCategory.id, learningRecord.quiz)
                .fetch();
    }
}
