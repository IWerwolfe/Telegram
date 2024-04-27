package com.supportbot.DTO;

import com.supportbot.components.Buttons;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

@Data
@AllArgsConstructor
public class CommandInfo {
    private String startMessage;
    private String errorMessage;
    private String regex;
    private Runnable handler;
    private ReplyKeyboard keyboard;

    public CommandInfo(String startMessage, String errorMessage, String regex, Runnable handler) {
        this(startMessage, errorMessage);
        this.regex = regex;
        this.handler = handler;
    }

    public CommandInfo(String startMessage, String errorMessage) {
        this.startMessage = startMessage;
        this.errorMessage = errorMessage;
    }

    public CommandInfo(String startMessage, String errorMessage, ReplyKeyboard keyboard) {
        this.startMessage = startMessage;
        this.errorMessage = errorMessage;
        this.keyboard = keyboard;
    }

    public CommandInfo() {
        this.keyboard = Buttons.keyboardMarkupCommands();
    }
}
