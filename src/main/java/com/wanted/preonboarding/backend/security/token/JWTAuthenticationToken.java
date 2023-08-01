package com.wanted.preonboarding.backend.security.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;


public class JWTAuthenticationToken extends AbstractAuthenticationToken {
//    private static final long serialVersionUID = 520L;
    private Object principal;
    private Object credentials;

    private String jsonWebToken;


    // 인증 이전 입력 정보 담는
    public JWTAuthenticationToken(String jsonWebToken) {
        super(null);
        this.setAuthenticated(false);

        this.jsonWebToken = jsonWebToken;
    }


    //인증 이후 결과 담는 (권한정보 추가)
    public JWTAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true);
    }

    public Object getCredentials() {
        return this.credentials;
    }

    public Object getPrincipal() {
        return this.principal;
    }

    public String getJsonWebToken() {
        return this.jsonWebToken;
    }


    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException("Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        } else {
            super.setAuthenticated(false);
        }
    }

    public void eraseCredentials() {
        super.eraseCredentials();
        this.credentials = null;
    }

}
