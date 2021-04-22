package com.example.certificationboard.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.orm.jpa.JpaTransactionManager;

import javax.naming.NamingException;

@Configuration
@EnableJpaAuditing
public class JpaConfig {

    @Bean("jpaTransactionManager")
    public JpaTransactionManager jpaTransactionManager() throws NamingException {
        return new JpaTransactionManager();
    }
}
