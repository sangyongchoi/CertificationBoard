package com.example.certificationboard.security.handler;

import com.example.certificationboard.common.util.DateUtil;
import com.example.certificationboard.member.application.LoginResponse;
import com.example.certificationboard.member.domain.Member;
import com.example.certificationboard.security.jwt.JWTGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LoginAuthHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;
    private final JWTGenerator jwtGenerator;

    public LoginAuthHandler(ObjectMapper objectMapper, JWTGenerator jwtGenerator) {
        this.objectMapper = objectMapper;
        this.jwtGenerator = jwtGenerator;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        final Member user = (Member) authentication.getPrincipal();
        Map<String, String> claim = getClaim(user);
        final String token = jwtGenerator.createToken(claim);
        LoginResponse loginResponse = new LoginResponse(user.getOrganizationId(), token);

        response.getWriter().println(objectMapper.writeValueAsString(loginResponse));
        response.setStatus(HttpStatus.OK.value());
    }

    private Map<String, String> getClaim(Member user) {
        Map<String, String> claim = new HashMap<>();
        claim.put("key", UUID.randomUUID() + DateUtil.nowToString());
        claim.put("userId", user.getId());

        return claim;
    }
}
