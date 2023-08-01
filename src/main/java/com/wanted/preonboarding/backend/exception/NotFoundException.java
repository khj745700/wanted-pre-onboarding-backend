package com.wanted.preonboarding.backend.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends CustomException{
    public NotFoundException() {
        super(HttpStatus.NOT_FOUND, "해당 자원을 찾을 수 없습니다.");
    }
}
