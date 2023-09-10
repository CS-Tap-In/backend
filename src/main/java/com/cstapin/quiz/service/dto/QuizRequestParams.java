package com.cstapin.quiz.service.dto;

import com.cstapin.quiz.domain.QuizStatus;
import com.cstapin.support.enums.ConditionYN;
import com.cstapin.support.service.dto.AbstractPageRequest;
import lombok.Setter;

@Setter
public class QuizRequestParams extends AbstractPageRequest {

    private String st;
    private String keyword;
    private Long category;
    private QuizStatus status;
    private ConditionYN rejected;

    public String getSearchType() {
        return st;
    }

    public Long getCategoryId() {
        return category;
    }

    public String getKeyword() {
        return keyword;
    }

    public QuizStatus getStatus() {
        return status;
    }

    public ConditionYN getRejected() {
        return rejected;
    }
}
