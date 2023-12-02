package com.telegrambot.app.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Data
@PropertySource("application.properties")
public class Connector1C {
    @Value("${Connector1C.url}")
    String url;
    @Value("${Connector1C.token}")
    String token;
    @Value("${Connector1C.login}")
    String login;
    @Value("${Connector1C.password}")
    String password;
}
