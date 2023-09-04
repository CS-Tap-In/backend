package com.cstapin.quiz.domain.search;

import com.querydsl.core.types.dsl.BooleanExpression;

import java.util.Arrays;
import java.util.function.Function;

import static com.cstapin.quiz.domain.QQuiz.quiz;

public enum QuizSearchType {
    AUTHOR("author", quiz.author.username::contains),
    TITLE("title", quiz.title::contains);

    private final String searchType;
    private final Function<String, BooleanExpression> function;

    QuizSearchType(String searchType, Function<String, BooleanExpression> function) {
        this.searchType = searchType;
        this.function = function;
    }

    public static BooleanExpression getConditionBySearchType(String searchType, String keyword) {
        return Arrays.stream(values()).filter(quizSearchType -> quizSearchType.searchType.equals(searchType))
                .findAny().map(quizSearchType -> quizSearchType.function.apply(keyword))
                .orElse(null);
    }
}
