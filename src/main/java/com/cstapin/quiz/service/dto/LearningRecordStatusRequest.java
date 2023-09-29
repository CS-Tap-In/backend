package com.cstapin.quiz.service.dto;

import com.cstapin.quiz.domain.LearningStatus;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class LearningRecordStatusRequest {

    @NotNull
    private LearningStatus learningStatus;
}
