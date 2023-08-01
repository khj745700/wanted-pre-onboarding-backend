package com.wanted.preonboarding.backend.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;


@Setter
@Getter
@NoArgsConstructor
public class CustomErrorResponse {

    private String errorMessage;

    private HttpStatus httpStatus;

    public CustomErrorResponse(HttpStatus httpStatus, String errorMessage){
        this.errorMessage = errorMessage;
        this.httpStatus = httpStatus;
    }
}
