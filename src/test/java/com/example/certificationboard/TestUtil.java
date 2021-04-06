package com.example.certificationboard;

import com.example.certificationboard.common.util.DateUtil;
import com.example.certificationboard.security.jwt.JWTGenerator;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TestUtil {

    public static String createToken(){
        JWTGenerator jwtGenerator = new JWTGenerator();
        Map<String, String> claim = new HashMap<>();
        claim.put("key", UUID.randomUUID() + DateUtil.nowToString());

        return jwtGenerator.createToken(claim);
    }
}
