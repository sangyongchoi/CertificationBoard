package com.example.certificationboard.security.exception;

import org.springframework.security.core.AuthenticationException;

public class JwtAuthenticationException extends AuthenticationException {
    private static final String ERROR_MESSAGE = "토큰정보가 잘못 되었습니다.";

    public JwtAuthenticationException() {
        super(ERROR_MESSAGE);
    }
}
