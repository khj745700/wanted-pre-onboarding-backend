package com.wanted.preonboarding.backend.exception;

import org.springframework.http.HttpStatus;

public class EditAuthenticationException extends CustomException{
    public EditAuthenticationException() {
        super(HttpStatus.FORBIDDEN, "수정할 수 없습니다. 잘못된 접근입니다.");
    }
}
