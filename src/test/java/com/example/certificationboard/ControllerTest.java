package com.example.certificationboard;

import com.example.certificationboard.config.TestConfig;
import com.example.certificationboard.security.config.SecurityConfig;
import com.example.certificationboard.security.handler.LoginAuthHandler;
import com.example.certificationboard.security.jwt.JWTGenerator;
import com.example.certificationboard.security.provider.JWTAuthenticationProvider;
import com.example.certificationboard.security.provider.LoginAuthenticationProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith({SpringExtension.class})
@Import({SecurityConfig.class, TestConfig.class})
public class ControllerTest {
    @Autowired
    LoginAuthenticationProvider loginAuthenticationProvider;

    @Autowired
    JWTGenerator jwtGenerator;

    @Autowired
    JWTAuthenticationProvider jwtAuthenticationProvider;

    @Autowired
    LoginAuthHandler loginAuthHandler;

    protected String jwt;

    @BeforeEach
    public void setup(){
        jwt = TestUtil.createToken();
    }
}