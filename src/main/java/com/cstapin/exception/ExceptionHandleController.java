package com.cstapin.exception;

import com.cstapin.exception.notfound.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionHandleController {

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ExceptionResponse> illegalStateExceptionHandle(IllegalStateException e) {
        log.warn("[ExceptionHandle]: IllegalStateException | msg: {}", e.getMessage());

        ExceptionResponse exceptionResponse = new ExceptionResponse("BAD", e.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> illegalArgumentExceptionHandle(IllegalArgumentException e) {
        log.warn("[ExceptionHandle]: IllegalArgumentException | msg: {}", e.getMessage());

        ExceptionResponse exceptionResponse = new ExceptionResponse("BAD", e.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> methodArgumentNotValidExceptionHandle(MethodArgumentNotValidException e) {
        log.warn("[ExceptionHandle]: MethodArgumentNotValidException | msg: {}", e.getMessage());

        ExceptionResponse exceptionResponse = new ExceptionResponse("BAD", "요청 값을 확인해주세요.");
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionResponse> httpMessageNotReadableExceptionHandle(HttpMessageNotReadableException e) {
        log.warn("[ExceptionHandle]: HttpMessageNotReadableException | msg: {}", e.getMessage());

        ExceptionResponse exceptionResponse = new ExceptionResponse("BAD", "요청 값을 확인해주세요.");
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionResponse> accessDeniedExceptionHandle(AccessDeniedException e) {
        log.warn("[ExceptionHandle]: AccessDeniedException | msg: {}", e.getMessage());

        ExceptionResponse exceptionResponse = new ExceptionResponse("FORBIDDEN", e.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionResponse> notFoundExceptionHandle(NotFoundException e) {
        log.warn("[ExceptionHandle]: AccessDeniedException | msg: {}", e.getMessage());

        ExceptionResponse exceptionResponse = new ExceptionResponse("BAD", e.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

}
