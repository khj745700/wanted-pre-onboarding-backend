package com.wanted.preonboarding.backend.aop;

import com.wanted.preonboarding.backend.annotation.LoginCheck;
import com.wanted.preonboarding.backend.exception.CustomErrorResponse;
import com.wanted.preonboarding.backend.exception.NotLoginException;
import com.wanted.preonboarding.backend.utils.ContextHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;
import org.springframework.web.ErrorResponseException;


@Aspect
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class LoginCheckAspect {

    @Around("@annotation(com.wanted.preonboarding.backend.annotation.LoginCheck)")
    private Object loginCheck(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        if(ContextHolder.getSession() == null) {
            throw new NotLoginException();
        }

        return proceedingJoinPoint.proceed();
    }
}
