package com.example.certificationboard.security.provider;

import com.example.certificationboard.security.jwt.JWTAuthenticationToken;
import com.example.certificationboard.security.jwt.JWTGenerator;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class JWTAuthenticationProvider implements AuthenticationProvider {

    private final JWTGenerator jwtGenerator;

    public JWTAuthenticationProvider(JWTGenerator jwtGenerator) {
        this.jwtGenerator = jwtGenerator;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String credentials = (String) authentication.getCredentials();
        final String key = jwtGenerator.authenticationVerify(credentials);

        return new JWTAuthenticationToken(null, credentials, key);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return JWTAuthenticationToken.class.isAssignableFrom(aClass);
    }

}
