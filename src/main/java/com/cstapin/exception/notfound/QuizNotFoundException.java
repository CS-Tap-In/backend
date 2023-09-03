package com.cstapin.exception.notfound;

public class QuizNotFoundException extends NotFoundException {

    private static final String MESSAGE = "해당하는 Quiz 가 없습니다.";

    public QuizNotFoundException() {
        super(MESSAGE);
    }
}
