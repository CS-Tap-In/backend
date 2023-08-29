package com.cstapin.quiz.service.query;

import com.cstapin.exception.notfound.QuizCategoryNotFoundException;
import com.cstapin.quiz.domain.QuizCategory;
import com.cstapin.quiz.domain.QuizCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuizCategoryQueryService {

    private final QuizCategoryRepository quizCategoryRepository;

    public QuizCategory findById(Long quizCategoryId) {
        return quizCategoryRepository.findById(quizCategoryId).orElseThrow(QuizCategoryNotFoundException::new);
    }
}
