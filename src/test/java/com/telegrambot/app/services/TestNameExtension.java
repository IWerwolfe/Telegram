package com.telegrambot.app.services;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

public class TestNameExtension implements TestWatcher {

    private static String testName;

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        TestWatcher.super.testFailed(context, cause);
    }

    public void testStarted(ExtensionContext context) {
        testName = context.getDisplayName();
    }

    public static String getTestName() {
        return testName;
    }
}