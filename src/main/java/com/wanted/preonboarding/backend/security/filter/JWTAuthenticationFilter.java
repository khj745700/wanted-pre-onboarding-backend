package com.wanted.preonboarding.backend.security.filter;


import com.wanted.preonboarding.backend.security.dto.properties.JWT;
import com.wanted.preonboarding.backend.security.token.JWTAuthenticationToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//OncePerRequestFilter : 한 요청에 대해 한 번만 처리 되는 것을 보장하는 필터 클래스

@AllArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = request.getHeader(JWT.ACCESS_TOKEN_HEADER);

        if(StringUtils.hasText(token) && token.startsWith(JWT.TOKEN_PREFIX)){
            try {
                Authentication jwtAuthenticationToken = new JWTAuthenticationToken(token.substring(JWT.TOKEN_PREFIX.length())); // prefix 'Bearer ' 제거 후 인증 토큰 생성
                Authentication authentication = authenticationManager.authenticate(jwtAuthenticationToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (AuthenticationException authenticationException) {
                Authentication anonymous = new AnonymousAuthenticationToken("NOT_LOGIN", null, null);
                SecurityContextHolder.getContext().setAuthentication(anonymous);

            }
        }
    try {
        filterChain.doFilter(request, response);
    }catch (Exception e){
        e.printStackTrace();
    }
    }

}

