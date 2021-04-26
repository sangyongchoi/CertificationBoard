package com.example.certificationboard.common;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.certificationboard.common.exception.ErrorResponse;
import com.example.certificationboard.member.exception.DuplicateUserException;
import com.example.certificationboard.post.exception.NotAllowedValueException;
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

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(JWTVerificationException.class)
    public ErrorResponse jwtVerifyExceptionHandle(JWTVerificationException e){
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotAllowedValueException.class)
    public ErrorResponse notAllowedValueExceptionHandle(NotAllowedValueException e) {
        return new ErrorResponse(e.getMessage());
    }

}
