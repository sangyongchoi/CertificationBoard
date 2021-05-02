package com.example.certificationboard;

import com.example.certificationboard.common.util.DateUtil;
import com.example.certificationboard.security.jwt.JWTGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TestUtil {

    public static String createToken(){
        JWTGenerator jwtGenerator = new JWTGenerator();
        Map<String, String> claim = new HashMap<>();
        claim.put("key", UUID.randomUUID() + DateUtil.nowToString());
        claim.put("userId", "csytest1");

        return jwtGenerator.createToken(claim);
    }

    @Test
    @DisplayName("토큰 테스트")
    public void create_token_test() throws Exception{
        JWTGenerator jwtGenerator = new JWTGenerator();
        Map<String, String> claim = new HashMap<>();
        claim.put("key", UUID.randomUUID() + DateUtil.nowToString());

        System.out.println(jwtGenerator.createToken(claim));
    }
}
