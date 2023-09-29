package com.cstapin.quiz.service.dto;

import com.cstapin.quiz.domain.DailySelectedQuizzes;
import com.cstapin.quiz.domain.Quiz;
import com.cstapin.quiz.domain.QuizCategory;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class DailyQuizzesSummaryResponse {

    private final int reviewQuizCount;
    private final int newQuizCount;
    private final List<QuizCategoryCountResponse> quizCategories;

    public DailyQuizzesSummaryResponse(int reviewQuizCount,
                                       int newQuizCount,
                                       List<QuizCategoryCountResponse> quizCategories) {
        this.reviewQuizCount = reviewQuizCount;
        this.newQuizCount = newQuizCount;
        this.quizCategories = quizCategories;
    }

    public static DailyQuizzesSummaryResponse from(DailySelectedQuizzes dailySelectedQuizzes) {
        List<QuizCategory> selectedQuizCategories = dailySelectedQuizzes.getTotalQuizzes().stream()
                .map(Quiz::getQuizCategory).collect(Collectors.toList());

        Map<QuizCategory, Integer> quizCategoryMap = new HashMap<>();

        selectedQuizCategories.forEach(quizCategory ->
                quizCategoryMap.put(quizCategory, quizCategoryMap.getOrDefault(quizCategory, 0) + 1));

        List<QuizCategoryCountResponse> selectedQuizCategoryCounts = quizCategoryMap.keySet().stream()
                .map(quizCategory -> new QuizCategoryCountResponse(quizCategory.getTitle(), quizCategoryMap.get(quizCategory)))
                .collect(Collectors.toList());

        return new DailyQuizzesSummaryResponse(
                dailySelectedQuizzes.getReviewQuizzesSize(),
                dailySelectedQuizzes.getNewQuizzesSize(),
                selectedQuizCategoryCounts);
    }
}
