package com.example.certificationboard.common;

import com.example.certificationboard.common.exception.ErrorResponse;
import com.example.certificationboard.member.exception.DuplicateUserException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DuplicateUserException.class)
    public ErrorResponse userDuplicateExceptionHandle(DuplicateUserException e) {
        return new ErrorResponse(e.getMessage());
    }

}
