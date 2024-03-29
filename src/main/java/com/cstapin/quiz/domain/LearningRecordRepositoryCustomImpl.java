package com.cstapin.quiz.domain;

import com.cstapin.quiz.service.dto.DailyQuizzesResponse;
import com.cstapin.quiz.service.dto.QDailyQuizzesResponse;
import com.cstapin.quiz.service.dto.QQuizCountByCategoryId;
import com.cstapin.quiz.service.dto.QuizCountByCategoryId;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.cstapin.quiz.domain.QLearningRecord.learningRecord;
import static com.cstapin.quiz.domain.QQuiz.quiz;
import static com.cstapin.quiz.domain.QQuizCategory.quizCategory;

@RequiredArgsConstructor
public class LearningRecordRepositoryCustomImpl implements LearningRecordRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<LearningRecord> findLatestLearningRecords(Long memberId, int size, LocalDateTime time) {
        return queryFactory
                .selectFrom(learningRecord)
                .where(
                        learningRecord.memberId.eq(memberId),
                        learningRecord.createdAt.before(time),
                        learningRecord.status.ne(LearningStatus.NONE)
                )
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
                        quiz.id,
                        quizCategory.title,
                        quiz.title,
                        quiz.problem,
                        quiz.answer
                ))
                .from(learningRecord)
                .join(learningRecord.quiz, quiz)
                .join(quiz.quizCategory, quizCategory)
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
                        learningRecord.count()))
                .from(learningRecord)
                .where(
                        learningRecord.memberId.eq(memberId),
                        learningRecord.status.ne(LearningStatus.NONE),
                        learningRecord.status.ne(LearningStatus.FAILURE)
                )
                .join(learningRecord.quiz, quiz)
                .join(quiz.quizCategory)
                .groupBy(learningRecord.quiz)
                .fetch();
    }

    @Override
    public List<LearningRecord> findLearningRecords(Long memberId, LocalDate date) {
        return queryFactory
                .selectFrom(learningRecord)
                .where(learningRecord.memberId.eq(memberId), learningRecord.createdAt.after(date.atStartOfDay()))
                .orderBy(learningRecord.createdAt.desc())
                .fetch();
    }

    @Override
    public List<Quiz> findCompleteQuiz(Long memberId, LocalDate date) {
        return queryFactory
                .select(quiz)
                .from(learningRecord)
                .where(
                        learningRecord.memberId.eq(memberId),
                        learningRecord.createdAt.after(date.atStartOfDay()),
                        learningRecord.status.eq(LearningStatus.SUCCESS)
                                .or(learningRecord.status.eq(LearningStatus.RECOVERY))
                )
                .join(learningRecord.quiz, quiz)
                .fetch();
    }
}
