package com.cstapin.quiz.service.query;

import com.cstapin.exception.notfound.QuizNotFoundException;
import com.cstapin.quiz.domain.Quiz;
import com.cstapin.quiz.domain.QuizRepository;
import com.cstapin.quiz.service.dto.QuizRequestParams;
import com.cstapin.quiz.service.dto.QuizzesResponse;
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

    public Page<QuizzesResponse> findQuizzes(QuizRequestParams requestParams) {
        return quizRepository.findAll(requestParams.getPageable())
                .map(QuizzesResponse::from);
    }
}
