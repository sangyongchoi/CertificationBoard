package com.example.certificationboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class CertificationboardApplication {

    public static void main(String[] args) {
        SpringApplication.run(CertificationboardApplication.class, args);
    }

}
