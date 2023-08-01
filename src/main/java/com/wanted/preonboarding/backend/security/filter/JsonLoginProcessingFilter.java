package com.wanted.preonboarding.backend.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wanted.preonboarding.backend.dto.LoginDto;
import com.wanted.preonboarding.backend.security.token.JsonAuthenticationToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.ObjectUtils;

import java.io.IOException;

//json으로 로그인 시도의 경우 처리 필터
public class JsonLoginProcessingFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public JsonLoginProcessingFilter(){
        super(new AntPathRequestMatcher("/login", "POST")); // 필터 동작 조건 1
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {

        if(!isJson(request)){ // 필터 동작 조건 2
            throw new BadCredentialsException("is not json");
        }

        // 두 조건 모두 통과 시 id/pw 통해 인증
        LoginDto loginDto = objectMapper.readValue(request.getReader(), LoginDto.class);//json 바디 가져와서 dto로 가공

        //빈 문자열 체크
        if(ObjectUtils.isEmpty(loginDto.getUsername()) || ObjectUtils.isEmpty(loginDto.getPassword())){
            throw new BadCredentialsException("id, password is null");
        }

        // 토큰 만들고 AuthenticationManager에 위임하여 인증 처리 진행
        JsonAuthenticationToken jsonAuthenticationToken = new JsonAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
        return getAuthenticationManager().authenticate(jsonAuthenticationToken);
    }

    private boolean isJson(HttpServletRequest request) {
        return request.getHeader("Content-Type").contains("application/json");
    }
}
