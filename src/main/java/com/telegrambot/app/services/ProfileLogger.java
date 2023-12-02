package com.telegrambot.app.services;

import com.telegrambot.app.config.BotConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProfileLogger {

    private final Environment environment;
    private final BotConfig botConfig;

    public void logActiveProfile() {

        String[] activeProfiles = environment.getActiveProfiles();
        String message = activeProfiles.length == 0 ?
                "No active profile set." :
                "Active profile(s): " + String.join(", ", activeProfiles);

        log.error(message);

        log.error("Active bot \"{}\"", botConfig.getBotName());
    }
}
