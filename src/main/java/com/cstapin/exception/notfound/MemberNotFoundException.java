package com.cstapin.exception.notfound;

public class MemberNotFoundException extends NotFoundException {
    private static final String MESSAGE = "해당하는 Member 가 없습니다.";

    public MemberNotFoundException() {
        super(MESSAGE);
    }

    public MemberNotFoundException(String message) {
        super(message);
    }
}
