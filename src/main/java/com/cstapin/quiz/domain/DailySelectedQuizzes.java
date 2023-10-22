package com.cstapin.quiz.domain;

import java.util.HashSet;
import java.util.Set;

public class DailySelectedQuizzes {
    private final Set<Quiz> reviewQuizzes = new HashSet<>();
    private final Set<Quiz> newQuizzes = new HashSet<>();
    private boolean firstTimeQuestionToday = true;

    public DailySelectedQuizzes() {
    }

    public void setFirstTimeQuestionToday(boolean firstTimeQuestionToday) {
        this.firstTimeQuestionToday = firstTimeQuestionToday;
    }

    public boolean isFirstTimeQuestionToday() {
        return firstTimeQuestionToday;
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

    public Set<Quiz> getTotalQuizzes() {
        Set<Quiz> totalQuizzes = new HashSet<>();
        totalQuizzes.addAll(reviewQuizzes);
        totalQuizzes.addAll(newQuizzes);
        return totalQuizzes;
    }
}
