package com.cstapin.quiz.service.query;

import com.cstapin.exception.notfound.QuizNotFoundException;
import com.cstapin.quiz.domain.Quiz;
import com.cstapin.quiz.domain.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuizQueryService {

    private final QuizRepository quizRepository;

    public Quiz findById(Long quizId) {
        return quizRepository.findById(quizId).orElseThrow(QuizNotFoundException::new);
    }
}
