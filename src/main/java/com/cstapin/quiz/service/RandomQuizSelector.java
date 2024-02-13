package com.cstapin.quiz.service;

import com.cstapin.quiz.domain.Quiz;
import com.cstapin.quiz.domain.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RandomQuizSelector {

    private final QuizRepository quizRepository;

    public List<Quiz> select(List<Long> quizCategoryIds, RandomSelector<Quiz> randomSelector, int size) {

        int countOfEachQuizCategory = Math.round((float) size / quizCategoryIds.size());

        List<Quiz> selectedQuizzes = quizCategoryIds.stream()
                .map(quizRepository::findByQuizCategoryId)
                .map(quizzes -> randomSelector.select(quizzes, countOfEachQuizCategory))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        removeExcessQuizzes(selectedQuizzes, size);

        Collections.shuffle(selectedQuizzes);

        return selectedQuizzes;
    }

    private void removeExcessQuizzes(List<Quiz> selectedQuizzes, int quizCount) {
        while (selectedQuizzes.size() > quizCount) {
            selectedQuizzes.remove(0);
        }
    }
}