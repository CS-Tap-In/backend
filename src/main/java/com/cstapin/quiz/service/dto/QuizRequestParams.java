package com.cstapin.quiz.service.dto;

import com.cstapin.support.service.dto.AbstractPageRequest;
import lombok.Setter;

@Setter
public class QuizRequestParams extends AbstractPageRequest {

    private String st;
    private String keyword;
    private Long category;

    public String getSearchType() {
        return st;
    }

    public Long getCategoryId() {
        return category;
    }

    public String getKeyword() {
        return keyword;
    }
}
