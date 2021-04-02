package com.example.certificationboard.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtGeneratorTest {

    @Test
    @DisplayName("JWT 생성 테스트")
    public void jwtTokenTest() {
        // Create JWT Token
        String token = JWT.create()
                .withSubject("helloworld")
                .withExpiresAt(new Date(System.currentTimeMillis() + 864000000))
                .sign(Algorithm.HMAC512("test".getBytes()));

        //jwt token header.payload.secret
        System.out.println(token);
    }

}