package com.cstapin.exception.notfound;

public class QuizCategoryNotFoundException extends NotFoundException {

    private static final String MESSAGE = "해당하는 QuizCategory 가 없습니다.";

    public QuizCategoryNotFoundException() {
        super(MESSAGE);
    }
}
