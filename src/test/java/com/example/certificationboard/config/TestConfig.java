package com.example.certificationboard.config;

import com.example.certificationboard.member.domain.Member;
import com.example.certificationboard.member.domain.MemberRepository;
import com.example.certificationboard.security.authentication.UserDetailService;
import com.example.certificationboard.security.handler.LoginAuthHandler;
import com.example.certificationboard.security.jwt.JWTGenerator;
import com.example.certificationboard.security.provider.JWTAuthenticationProvider;
import com.example.certificationboard.security.provider.LoginAuthenticationProvider;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

@TestConfiguration
public class TestConfig {

    @Bean
    public JWTGenerator jwtGenerator(){
        return new JWTGenerator();
    }

    @Bean
    public JWTAuthenticationProvider jwtAuthenticationProvider(JWTGenerator jwtGenerator) {
        return new JWTAuthenticationProvider(jwtGenerator);
    }

    @Bean
    public LoginAuthHandler loginAuthHandler(JWTGenerator jwtGenerator) {
        return new LoginAuthHandler(jwtGenerator);
    }

    @Bean
    public LoginAuthenticationProvider loginAuthenticationProvider(PasswordEncoder passwordEncoder, UserDetailsService userDetailService) {
        return new LoginAuthenticationProvider(userDetailService, passwordEncoder);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(MemberRepository memberRepository) {
        return new UserDetailService(memberRepository);
    }

    @Bean
    public MemberRepository memberRepository() {
        return new MemberRepository() {
            @Override
            public List<Member> findAll() {
                return null;
            }

            @Override
            public List<Member> findAll(Sort sort) {
                return null;
            }

            @Override
            public List<Member> findAllById(Iterable<String> iterable) {
                return null;
            }

            @Override
            public <S extends Member> List<S> saveAll(Iterable<S> iterable) {
                return null;
            }

            @Override
            public void flush() {

            }

            @Override
            public <S extends Member> S saveAndFlush(S s) {
                return null;
            }

            @Override
            public void deleteInBatch(Iterable<Member> iterable) {

            }

            @Override
            public void deleteAllInBatch() {

            }

            @Override
            public Member getOne(String s) {
                return null;
            }

            @Override
            public <S extends Member> List<S> findAll(Example<S> example) {
                return null;
            }

            @Override
            public <S extends Member> List<S> findAll(Example<S> example, Sort sort) {
                return null;
            }

            @Override
            public Page<Member> findAll(Pageable pageable) {
                return null;
            }

            @Override
            public <S extends Member> S save(S s) {
                return null;
            }

            @Override
            public Optional<Member> findById(String s) {
                return Optional.empty();
            }

            @Override
            public boolean existsById(String s) {
                return false;
            }

            @Override
            public long count() {
                return 0;
            }

            @Override
            public void deleteById(String s) {

            }

            @Override
            public void delete(Member member) {

            }

            @Override
            public void deleteAll(Iterable<? extends Member> iterable) {

            }

            @Override
            public void deleteAll() {

            }

            @Override
            public <S extends Member> Optional<S> findOne(Example<S> example) {
                return Optional.empty();
            }

            @Override
            public <S extends Member> Page<S> findAll(Example<S> example, Pageable pageable) {
                return null;
            }

            @Override
            public <S extends Member> long count(Example<S> example) {
                return 0;
            }

            @Override
            public <S extends Member> boolean exists(Example<S> example) {
                return false;
            }
        };
    }

}
