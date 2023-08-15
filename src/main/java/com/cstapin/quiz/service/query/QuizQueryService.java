package com.cstapin.quiz.service.query;

import com.cstapin.exception.notfound.QuizNotFoundException;
import com.cstapin.quiz.domain.Quiz;
import com.cstapin.quiz.domain.QuizRepository;
import com.cstapin.quiz.service.dto.QuizRequestParams;
import com.cstapin.quiz.service.dto.QuizResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuizQueryService {

    private final QuizRepository quizRepository;

    public Quiz findById(Long quizId) {
        return quizRepository.findById(quizId).orElseThrow(QuizNotFoundException::new);
    }

    public Page<QuizResponse> findQuizzes(QuizRequestParams requestParams) {
        return quizRepository.findAll(requestParams.getPageable())
                .map(QuizResponse::from);
    }
}
