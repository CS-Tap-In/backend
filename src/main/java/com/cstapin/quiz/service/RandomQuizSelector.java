package com.cstapin.quiz.service;

import com.cstapin.quiz.domain.Quiz;
import com.cstapin.quiz.domain.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RandomQuizSelector {

    private static final int TOTAL_QUIZ_COUNT = 50;
    private final QuizRepository quizRepository;

    public List<Quiz> select(List<Long> quizCategoryIds, RandomSelector<Quiz> randomSelector) {

        int countOfEachQuizCategory = Math.round((float) TOTAL_QUIZ_COUNT / quizCategoryIds.size());

        List<Quiz> selectedQuizzes = quizCategoryIds.stream()
                .map(quizRepository::findByQuizCategoryId)
                .map(quizzes -> randomSelector.select(quizzes, countOfEachQuizCategory))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        removeExcessQuizzes(selectedQuizzes);

        return selectedQuizzes;
    }

    private void removeExcessQuizzes(List<Quiz> selectedQuizzes) {
        while (selectedQuizzes.size() > TOTAL_QUIZ_COUNT) {
            selectedQuizzes.remove(0);
        }
    }
}