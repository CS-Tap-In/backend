package com.cstapin.exception.notfound;

public class TokenNotFoundException extends NotFoundException {
    private static final String MESSAGE = "해당하는 Token 이 없습니다.";

    public TokenNotFoundException() {
        super(MESSAGE);
    }

    public TokenNotFoundException(String message) {
        super(message);
    }
}
