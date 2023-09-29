package com.cstapin.quiz.domain;

import com.cstapin.quiz.service.dto.QuizCountByCategoryId;
import com.cstapin.quiz.service.dto.QuizRequestParams;
import com.cstapin.quiz.service.dto.QuizzesResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface QuizRepositoryCustom {
    Page<QuizzesResponse> findQuizzes(QuizRequestParams params);

    List<QuizCountByCategoryId> findQuizCountByQuizCategory();
}
