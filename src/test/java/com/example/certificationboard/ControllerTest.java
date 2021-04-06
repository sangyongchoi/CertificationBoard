package com.example.certificationboard;

import com.example.certificationboard.security.handler.LoginAuthHandler;
import com.example.certificationboard.security.jwt.JWTAuthenticationToken;
import com.example.certificationboard.security.jwt.JWTGenerator;
import com.example.certificationboard.security.provider.JWTAuthenticationProvider;
import com.example.certificationboard.security.provider.LoginAuthenticationProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith({SpringExtension.class})
public class ControllerTest {
    @MockBean
    LoginAuthenticationProvider loginAuthenticationProvider;

    @MockBean
    JWTGenerator jwtGenerator;

    @MockBean
    LoginAuthHandler loginAuthHandler;

    @MockBean
    JWTAuthenticationProvider jwtAuthenticationProvider;

    protected String jwt;

    @BeforeEach
    public void setup(){
        JWTGenerator generator = new JWTGenerator();
        jwt = TestUtil.createToken();
        given(jwtAuthenticationProvider.supports(JWTAuthenticationToken.class)).willReturn(true);
        given(jwtAuthenticationProvider.authenticate(any())).willReturn(new JWTAuthenticationToken(null, jwt, generator.authenticationVerify(jwt)));
    }
}
