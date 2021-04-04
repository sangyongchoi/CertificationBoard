package com.example.certificationboard.security.config;

import com.example.certificationboard.security.filter.JWTFilter;
import com.example.certificationboard.security.handler.LoginAuthHandler;
import com.example.certificationboard.security.jwt.JWTGenerator;
import com.example.certificationboard.security.matcher.SkipPathRequestMatcher;
import com.example.certificationboard.security.provider.JWTAuthenticationProvider;
import com.example.certificationboard.security.provider.LoginAuthenticationProvider;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String[] PUBLIC_URLS = {"/", "/signup", "/login"};

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf().disable()
                .formLogin()
                    .successHandler(loginAuthHandler())
                    .permitAll()
                .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                    .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class)
        ;

        final ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry expressionInterceptUrlRegistry = http.authorizeRequests();
        Arrays.stream(PUBLIC_URLS).forEach(url -> expressionInterceptUrlRegistry.antMatchers(url).permitAll());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(loginAuthenticationProvider());
        auth.authenticationProvider(jwtAuthenticationProvider());
    }

    public JWTFilter jwtFilter() throws Exception {
        String[] skipUrls = addAll(PUBLIC_URLS);
        JWTFilter jwtFilter = new JWTFilter(new SkipPathRequestMatcher(skipUrls));
        jwtFilter.setAuthenticationManager(authenticationManager());

        return jwtFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public LoginAuthHandler loginAuthHandler() {
        return new LoginAuthHandler(jwtGenerator());
    }

    @Bean
    public LoginAuthenticationProvider loginAuthenticationProvider() {
        return new LoginAuthenticationProvider(passwordEncoder());
    }

    @Bean
    public JWTAuthenticationProvider jwtAuthenticationProvider() {
        return new JWTAuthenticationProvider(jwtGenerator());
    }

    @Bean
    public JWTGenerator jwtGenerator(){
        return new JWTGenerator();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


    private static <T> T[] addAll(T[] firstArr, T[]... concatArgs) {
        T[] concatArr = firstArr;
        for (T[] t : concatArgs) {
            concatArr = ArrayUtils.addAll(concatArr, t);
        }
        return concatArr;
    }
}
