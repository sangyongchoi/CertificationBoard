package com.example.certificationboard.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JWTGeneratorTest {



    @Test
    @DisplayName("JWT 생성 테스트")
    public void jwtTokenTest() {
        String expected = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJoZWxsb3dvcmxkIn0.Jrb8dyTqbOT4R02dzIpDmrUKusbnXVG20MsqadUzSTrBTKTZakaHQBtuj-tJgfg0qLNZKFppY4keaAS_nuCPnQ";
        String token = JWT.create()
                .withSubject("helloworld")
                //.withExpiresAt(new Date(System.currentTimeMillis() + 864000000))
                .sign(Algorithm.HMAC512("test".getBytes()));

        assertEquals(expected, token);
    }

    @Test
    public void test() throws Exception{
        final LocalDateTime now = LocalDateTime.now();
        System.out.println("" + now.getYear() + now.getMonth().getValue() + now.getDayOfMonth() + now.getHour() + now.getMinute() + now.getSecond() + now.getNano());
    }

}