package com.wanted.preonboarding.backend.exception;

import org.springframework.http.HttpStatus;

public class NotLoginException extends CustomException{
    public NotLoginException() {
        super(HttpStatus.UNAUTHORIZED, "로그인 후 이용해주세요");
    }
}
