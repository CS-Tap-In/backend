package com.cstapin.exception;

import lombok.Getter;

@Getter
public class ExceptionResponse {

    private final String code;
    private final String message;

    public ExceptionResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String convertToJson() {
        return "{" +
                "\"code\": " + "\"" + code + "\",\n" +
                "\"message\": " + "\"" + message + "\"\n" +
                "}";
    }
}
