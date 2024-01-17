package com.supportbot.utils;

import com.supportbot.model.types.Reference;
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

    public static <R extends Reference> String getNameRef(R ref) {
        if (ref == null) {
            return "Unknown";
        }
        if (ref.getName() == null) {
            return ref.getClass().getSimpleName() + " " + ref.getId();
        }
        return ref.getName();
    }
}
