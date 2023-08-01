package com.wanted.preonboarding.backend.security.provider;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.wanted.preonboarding.backend.security.service.MemberContext;
import com.wanted.preonboarding.backend.security.token.JWTAuthenticationToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Map;

@Slf4j
public class JWTAuthenticationProvider implements AuthenticationProvider {


    private UserDetailsService userDetailsService;

    @Value("${jwt.issuer}") private String issuer;
    @Value("${jwt.secret-key}") private String secretKey;


    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {

        String username = verifyToken((JWTAuthenticationToken)auth);

        MemberContext memberContext = (MemberContext) userDetailsService.loadUserByUsername(username);

        return new JWTAuthenticationToken(memberContext.getMember(), null, memberContext.getAuthorities());
    }

    private String verifyToken(JWTAuthenticationToken jwtToken){

        String accountId;

        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);

            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build();

            DecodedJWT jwt = verifier.verify(jwtToken.getJsonWebToken());

            Map<String, Claim> claims = jwt.getClaims();
            accountId = claims.get("username").asString();

        } catch(SignatureVerificationException exception) {
            log.warn(exception.getMessage());
            throw new BadCredentialsException("token signature is invalid.");

        } catch(TokenExpiredException exception) {
            log.warn(exception.getMessage());
            throw new BadCredentialsException("expired token");

        } catch (JWTVerificationException exception){
            log.warn(exception.getMessage());
            throw new BadCredentialsException("invalid token");
        }

        return accountId;
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return JWTAuthenticationToken.class.isAssignableFrom(authentication);
    }

    public void setUserDetailsService(UserDetailsService service) {
        this.userDetailsService = service;
    }
}
