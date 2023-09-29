package com.cstapin.exception.notfound;

public class LearningRecordNotFoundException extends NotFoundException{
    private static final String MESSAGE = "해당하는 LearningRecord 가 없습니다.";

    public LearningRecordNotFoundException() {
        super(MESSAGE);
    }
}
