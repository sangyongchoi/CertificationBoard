package com.example.certificationboard.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;
import java.util.Map;

public class JwtGenerator {

    private static final int EXPIRE_TIME = 864000000;

    public String createToken(Map<String, String> claim) {
        final JWTCreator.Builder jwtBuilder = JWT.create()
                .withExpiresAt(getExpireDate());

        claim.forEach(jwtBuilder::withClaim);

        return jwtBuilder.sign(getAlgorithm());
    }

    private Date getExpireDate() {
        return new Date(System.currentTimeMillis() + EXPIRE_TIME);
    }

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC512("test".getBytes());
    }
}
