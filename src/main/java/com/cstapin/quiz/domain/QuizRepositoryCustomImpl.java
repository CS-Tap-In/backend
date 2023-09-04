package com.cstapin.quiz.domain;

import com.cstapin.quiz.domain.search.QuizSearchType;
import com.cstapin.quiz.service.dto.QQuizzesResponse;
import com.cstapin.quiz.service.dto.QuizRequestParams;
import com.cstapin.quiz.service.dto.QuizzesResponse;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static com.cstapin.quiz.domain.QQuiz.quiz;
import static com.cstapin.quiz.domain.QQuizCategory.quizCategory;
import static com.cstapin.member.domain.QMember.member;

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
                        quiz.createdAt))
                .from(quiz)
                .join(quiz.quizCategory, quizCategory)
                .join(quiz.author, member)
                .where(
                        QuizSearchType.getConditionBySearchType(params.getSearchType(), params.getKeyword()),
                        eqCategoryId(params.getCategoryId()))
                .limit(params.getPageable().getPageSize())
                .offset(params.getPageable().getOffset())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(quiz.count())
                .from(quiz)
                .join(quiz.author, member)
                .where(
                        QuizSearchType.getConditionBySearchType(params.getSearchType(), params.getKeyword()),
                        eqCategoryId(params.getCategoryId())
                );

        return PageableExecutionUtils.getPage(content, params.getPageable(), countQuery::fetchOne);
    }

    private BooleanExpression eqCategoryId(Long categoryId) {
        if (categoryId != null) {
            return quiz.quizCategory.id.eq(categoryId);
        }
        return null;
    }
}
