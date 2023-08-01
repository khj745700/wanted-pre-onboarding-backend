package com.wanted.preonboarding.backend.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanted.preonboarding.backend.entity.Member;
import com.wanted.preonboarding.backend.security.dto.JWTDTO;
import com.wanted.preonboarding.backend.security.dto.properties.JWT;
import com.wanted.preonboarding.backend.security.jwt.JWTFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

public class JsonAuthSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private com.wanted.preonboarding.backend.security.jwt.JWTFactory JWTFactory;
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res, Authentication auth) throws IOException, ServletException {

        Member userLogin = (Member) auth.getPrincipal(); //인증 객체로부터 인증 성공 시 저장한 Account 객체 받아오기
        res.setStatus(HttpStatus.OK.value());
        res.setContentType(MediaType.APPLICATION_JSON_VALUE);

        //json web token 생성
        JWTDTO jwtDto = JWTDTO.builder()
                        .accessToken(JWTFactory.createAccessToken(userLogin))
                        //.refreshToken(JWTFactory.createRefreshToken(userLogin))
                .build();

        res.setHeader(JWT.ACCESS_TOKEN_HEADER, JWT.TOKEN_PREFIX+jwtDto.getAccessToken());
        //res.setHeader(JWT.REFRESH_TOKEN_HEADER, JWT.TOKEN_PREFIX+jwtDto.getRefreshToken());
    }

}
