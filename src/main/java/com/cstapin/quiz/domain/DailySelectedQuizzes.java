package com.cstapin.quiz.domain;

import java.util.HashSet;
import java.util.Set;

public class DailySelectedQuizzes {
    private final Set<Quiz> reviewQuizzes = new HashSet<>();
    private final Set<Quiz> newQuizzes = new HashSet<>();
    private final boolean firstTimeQuizToday;

    public DailySelectedQuizzes(boolean firstTimeQuizToday) {
        this.firstTimeQuizToday = firstTimeQuizToday;
    }

    public void addReviewQuiz(Quiz quiz) {
        reviewQuizzes.add(quiz);
    }

    public void addNewQuiz(Quiz quiz) {
        newQuizzes.add(quiz);
    }

    public int getReviewQuizzesSize() {
        return reviewQuizzes.size();
    }

    public int getNewQuizzesSize() {
        return newQuizzes.size();
    }

    public int getTotalQuizzesSize() {
        return getNewQuizzesSize() + getReviewQuizzesSize();
    }

    public boolean isFirstTimeQuizToday() {
        return firstTimeQuizToday;
    }

    public Set<Quiz> getTotalQuizzes() {
        Set<Quiz> totalQuizzes = new HashSet<>();
        totalQuizzes.addAll(reviewQuizzes);
        totalQuizzes.addAll(newQuizzes);
        return totalQuizzes;
    }
}
