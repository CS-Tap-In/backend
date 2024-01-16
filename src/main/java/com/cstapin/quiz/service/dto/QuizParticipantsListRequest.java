package com.cstapin.quiz.service.dto;

import com.cstapin.support.service.dto.AbstractPageRequest;
import lombok.Getter;
import lombok.Setter;

import java.time.YearMonth;

@Getter
@Setter
public class QuizParticipantsListRequest extends AbstractPageRequest {

    private YearMonth ym = YearMonth.now();
}
