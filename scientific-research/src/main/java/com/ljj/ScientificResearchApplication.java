package com.ljj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class ScientificResearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScientificResearchApplication.class, args);
    }

}
