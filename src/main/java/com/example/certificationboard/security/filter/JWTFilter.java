package com.example.certificationboard.security.filter;

import com.example.certificationboard.security.jwt.JWTAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTFilter extends AbstractAuthenticationProcessingFilter {

    private static final String AUTH_HEADER_NAME = "authorization";

    public JWTFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        final String jwt = request.getHeader(AUTH_HEADER_NAME);
        if (StringUtils.hasLength(jwt)) {
            return null;
        }

        return getAuthenticationManager().authenticate(new JWTAuthenticationToken(jwt));
    }
}
