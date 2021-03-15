package com.example.certificationboard.member.exception;

public class DuplicateUserException extends IllegalArgumentException{

    public static final String DUPLICATED_USER_ID = "이미 존재하는 계정입니다.";

    public DuplicateUserException(String s) {
        super(s);
    }
}
