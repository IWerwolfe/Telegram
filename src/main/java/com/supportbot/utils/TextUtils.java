package com.supportbot.utils;

import com.supportbot.model.types.Reference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Arrays;
import java.util.Locale;

@Service
@Slf4j
public class TextUtils {

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter
            .ofLocalizedDate(FormatStyle.MEDIUM)
            .withLocale(Locale.getDefault());

    private static boolean isCorrect(Object object) {
        return object instanceof String ?
                !((String) object).isEmpty() : object != null;
    }

    public static String assembleString(Object... args) {

        if (args.length == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        Arrays.stream(args)
                .filter(TextUtils::isCorrect)
                .forEach(arg -> builder.append(arg).append(" "));
        return builder.toString().trim();
    }

    public String collectMessage(String logMessage, String... arguments) {
        try {
            return String.format(logMessage, arguments);
        } catch (Exception e) {
            log.error(e.getMessage());
            return e.getMessage();
        }
    }

    public String shortenText(String text, int size) {

        if (text == null || text.isEmpty()) {
            return "";
        }

        return text.length() < size ?
                text :
                text.substring(0, size - 3) + "...";
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
