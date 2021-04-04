package com.example.certificationboard;

import com.example.certificationboard.security.handler.LoginAuthHandler;
import com.example.certificationboard.security.provider.JWTAuthenticationProvider;
import com.example.certificationboard.security.provider.LoginAuthenticationProvider;
import org.springframework.boot.test.mock.mockito.MockBean;

public class ControllerTest {
    @MockBean
    LoginAuthenticationProvider loginAuthenticationProvider;

    @MockBean
    JWTAuthenticationProvider jwtAuthenticationProvider;

    @MockBean
    LoginAuthHandler loginAuthHandler;
}
