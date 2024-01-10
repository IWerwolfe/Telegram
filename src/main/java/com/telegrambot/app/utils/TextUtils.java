package com.telegrambot.app.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TextUtils {

    public String collectMessage(String logMessage, String... arguments) {
        try {
            return String.format(logMessage, arguments);
        } catch (Exception e) {
            log.error(e.getMessage());
            return e.getMessage();
        }
    }
}
