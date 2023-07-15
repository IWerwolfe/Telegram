package com.telegrambot.app.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

@Data
@AllArgsConstructor
public class SubCommandInfo {
    private final String startMessage;
    private final Runnable parent;
    private String errorMessage;
    private String nextSumCommand;
    private String regex;
    private Runnable handler;
    private ReplyKeyboard keyboard;

    public SubCommandInfo(String startMessage, String errorMessage, Runnable parent, String nextSumCommand, String regex, Runnable handler) {
        this.startMessage = startMessage;
        this.errorMessage = errorMessage;
        this.parent = parent;
        this.nextSumCommand = nextSumCommand;
        this.regex = regex;
        this.handler = handler;
    }

    public SubCommandInfo(String startMessage, String errorMessage, Runnable parent, String nextSumCommand) {
        this.startMessage = startMessage;
        this.errorMessage = errorMessage;
        this.parent = parent;
        this.nextSumCommand = nextSumCommand;
    }

    public SubCommandInfo(String startMessage, Runnable parent, String nextSumCommand) {
        this.startMessage = startMessage;
        this.parent = parent;
        this.nextSumCommand = nextSumCommand;
    }

    public SubCommandInfo(String startMessage, String errorMessage, Runnable parent) {
        this.startMessage = startMessage;
        this.errorMessage = errorMessage;
        this.parent = parent;
    }

    public SubCommandInfo(String startMessage, String errorMessage, Runnable parent, String nextSumCommand, String regex) {
        this.startMessage = startMessage;
        this.errorMessage = errorMessage;
        this.parent = parent;
        this.nextSumCommand = nextSumCommand;
        this.regex = regex;
    }

    public SubCommandInfo(String startMessage, String errorMessage, Runnable parent, String nextSumCommand, String regex, ReplyKeyboard keyboard) {
        this.startMessage = startMessage;
        this.errorMessage = errorMessage;
        this.parent = parent;
        this.nextSumCommand = nextSumCommand;
        this.regex = regex;
        this.keyboard = keyboard;
    }
}
