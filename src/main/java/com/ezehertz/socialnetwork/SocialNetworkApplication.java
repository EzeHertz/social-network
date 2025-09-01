package com.ezehertz.socialnetwork;

import com.ezehertz.socialnetwork.domain.common.repositoryFactory.RepositoryFactory;
import com.ezehertz.socialnetwork.infrastructure.jdbc.JdbcRepositoryFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication(scanBasePackages = "com.ezehertz.socialnetwork")
public class SocialNetworkApplication {

    public static void main(String[] args) {
        SpringApplication.run(SocialNetworkApplication.class, args);
        System.out.println("API started!");
    }

    @Bean
    public RepositoryFactory repositoryFactory(JdbcTemplate jdbc) {
        return new JdbcRepositoryFactory(jdbc);
    }
}
