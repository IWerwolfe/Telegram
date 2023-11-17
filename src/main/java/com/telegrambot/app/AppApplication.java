package com.telegrambot.app;

import com.telegrambot.app.config.Initializer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.telegrambot.app.repositories")
@EnableCaching
@RequiredArgsConstructor
public class AppApplication {
    private final static Initializer initializer = new Initializer();

    public static void main(String[] args) {
        initializer.init();
        SpringApplication.run(AppApplication.class, args);
    }
}
