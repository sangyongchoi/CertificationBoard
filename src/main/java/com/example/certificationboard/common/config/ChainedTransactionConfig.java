package com.example.certificationboard.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class ChainedTransactionConfig {

    private final MongoTransactionManager mongoTransactionManager;
    private final JpaTransactionManager jpaTransactionManager;

    public ChainedTransactionConfig(MongoTransactionManager mongoTransactionManager, JpaTransactionManager jpaTransactionManager) {
        this.mongoTransactionManager = mongoTransactionManager;
        this.jpaTransactionManager = jpaTransactionManager;
    }

    @Bean("transactionManager")
    @Primary
    public PlatformTransactionManager transactionManager() {
        return new ChainedTransactionManager(jpaTransactionManager, mongoTransactionManager);
    }
}
