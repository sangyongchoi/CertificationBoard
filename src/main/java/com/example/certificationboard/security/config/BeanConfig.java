package com.example.certificationboard.security.config;

import com.example.certificationboard.security.handler.LoginAuthHandler;
import com.example.certificationboard.security.jwt.JWTGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BeanConfig {
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public LoginAuthHandler loginAuthHandler(ObjectMapper objectMapper, JWTGenerator jwtGenerator) {
        return new LoginAuthHandler(objectMapper, jwtGenerator);
    }

    @Bean
    public JWTGenerator jwtGenerator(){
        return new JWTGenerator();
    }
}
