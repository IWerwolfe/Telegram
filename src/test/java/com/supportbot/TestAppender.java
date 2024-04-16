package com.supportbot;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

import java.util.ArrayList;
import java.util.List;

public class TestAppender extends AppenderBase<ILoggingEvent> {
    private final List<String> logMessages = new ArrayList<>();

    @Override
    protected void append(ILoggingEvent event) {
        logMessages.add(event.getMessage());
    }

    public boolean logContains(String message) {
        return logMessages.contains(message);
    }
}
