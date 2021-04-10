package com.example.certificationboard.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.Map;

public class JWTGenerator {

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

    public String authenticationVerify(String token) {
        try {
            tokenVerify(token);
            final Map<String, Claim> tokenClaim = getTokenClaim(token);
            return tokenClaim.get("key").asString();
        } catch (JWTVerificationException e) {
            throw e;
        }
    }

    private void tokenVerify(String token){
        final JWTVerifier build = JWT.require(getAlgorithm()).build();
        build.verify(token);
    }

    private Map<String, Claim> getTokenClaim(String token){
        final DecodedJWT decodedJWT = tokenToJwt(token);
        return decodedJWT.getClaims();
    }

    private DecodedJWT tokenToJwt(String token) {
        return JWT.decode(token);
    }

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC512("test".getBytes());
    }
}
