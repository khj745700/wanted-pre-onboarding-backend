package com.wanted.preonboarding.backend.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.wanted.preonboarding.backend.entity.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;


@Slf4j
@Component
public class JWTFactory {

    private final int SECOND = 1000;
    private final int MINUTE = SECOND * 60;

    private final String issuer;
    private final String secretKey;
    private final String refreshSecretKey;
    private final long expiryPeriod;
    private final long refreshExpiryPeriod;

    public JWTFactory(@Value("${jwt.issuer}") String issuer,
                      @Value("${jwt.secret-key}") String secretKey,
                      @Value("${jwt.refresh-secret-key}") String refreshSecretKey,
                      @Value("${jwt.expiry-period}") long expiryPeriod,
                      @Value("${jwt.refresh-expiry-period}") long refreshExpiryPeriod) {
        this.issuer = issuer;
        this.secretKey = secretKey;
        this.refreshSecretKey = refreshSecretKey;
        this.expiryPeriod = expiryPeriod;
        this.refreshExpiryPeriod = refreshExpiryPeriod;
    }


    public String createAccessToken(Member member){
        return createToken(member, secretKey, expiryPeriod);
    }

    public String createRefreshToken(Member member){
        return createToken(member, refreshSecretKey, refreshExpiryPeriod);
    }

    private String createToken(Member member, String key, long period) {

        String token = null;

        try {
            Algorithm algorithm = Algorithm.HMAC256(key);
            token = JWT.create() // Token Builder
                    .withIssuer(issuer)
                    .withClaim("username", member.getUsername())
                    .withExpiresAt(createExpirationDate(period))
                    .sign(algorithm);

        } catch (JWTCreationException exception){
            //Invalid Signing configuration / Couldn't convert Claims.
            log.error(exception.getMessage());
        }

        log.info("created a token for " + member.getUsername());

        return token;
    }

    private Date createExpirationDate(long period){
        Date current = new Date();
        return new Date(current.getTime() + period * MINUTE);
    }

}
