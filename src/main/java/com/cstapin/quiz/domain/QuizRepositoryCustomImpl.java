package com.cstapin.quiz.domain;

import com.cstapin.quiz.domain.search.QuizSearchType;
import com.cstapin.quiz.service.dto.*;
import com.cstapin.support.enums.ConditionYN;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.cstapin.member.persistence.QMember.member;
import static com.cstapin.quiz.domain.QQuiz.quiz;
import static com.cstapin.quiz.domain.QQuizCategory.quizCategory;

@RequiredArgsConstructor
public class QuizRepositoryCustomImpl implements QuizRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public Page<QuizzesResponse> findQuizzes(QuizRequestParams params) {
        List<QuizzesResponse> content = queryFactory
                .select(new QQuizzesResponse(
                        quiz.quizCategory.id,
                        quiz.quizCategory.title,
                        quiz.id,
                        quiz.title,
                        quiz.problem,
                        quiz.status,
                        quiz.createdAt))
                .from(quiz)
                .join(quiz.quizCategory, quizCategory)
                .join(quiz.author, member)
                .where(
                        QuizSearchType.getConditionBySearchType(params.getSearchType(), params.getKeyword()),
                        eqCategoryId(params.getCategoryId()),
                        getStatusCondition(params.getStatus(), params.getRejected()))
                .limit(params.getPageable().getPageSize())
                .offset(params.getPageable().getOffset())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(quiz.count())
                .from(quiz)
                .join(quiz.author, member)
                .where(
                        QuizSearchType.getConditionBySearchType(params.getSearchType(), params.getKeyword()),
                        eqCategoryId(params.getCategoryId()),
                        getStatusCondition(params.getStatus(), params.getRejected()));

        return PageableExecutionUtils.getPage(content, params.getPageable(), countQuery::fetchOne);
    }

    @Override
    public List<QuizCountByCategoryId> findQuizCountByQuizCategory() {
        return queryFactory
                .select(new QQuizCountByCategoryId(quizCategory.title, quizCategory.id, quiz.count()))
                .from(quiz)
                .join(quiz.quizCategory, quizCategory)
                .where(quiz.status.eq(QuizStatus.PUBLIC),
                        quizCategory.status.eq(QuizCategoryStatus.PUBLIC))
                .groupBy(quiz.quizCategory.id)
                .fetch();
    }

    private Predicate getStatusCondition(QuizStatus status, ConditionYN rejected) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (status != null && !status.equals(QuizStatus.DELETED) && !status.equals(QuizStatus.REJECTED)) {
            booleanBuilder.or(quiz.status.eq(status));
        }
        if (status == null) {
            booleanBuilder.or(quiz.status.eq(QuizStatus.PUBLIC)).or(quiz.status.eq(QuizStatus.PRIVATE));
        }
        if (ConditionYN.Y.equals(rejected)) {
            booleanBuilder.or(quiz.status.eq(QuizStatus.REJECTED));
        }
        return booleanBuilder.getValue();
    }

    private BooleanExpression eqCategoryId(Long categoryId) {
        if (categoryId != null) {
            return quiz.quizCategory.id.eq(categoryId);
        }
        return null;
    }
}
