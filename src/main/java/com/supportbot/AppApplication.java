package com.supportbot;

import com.supportbot.config.Initializer;
import com.supportbot.services.ProfileLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.supportbot.repositories")
@EnableCaching
@RequiredArgsConstructor
public class AppApplication {
    private final static Initializer initializer = new Initializer();

    public static void main(String[] args) {
        initializer.init();
        SpringApplication.run(AppApplication.class, args);
    }

    @Bean
    public CommandLineRunner logActiveProfile(ProfileLogger profileLogger) {
        return args -> profileLogger.logActiveProfile();
    }
}
