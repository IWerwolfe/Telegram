package com.supportbot.DTO;

import com.supportbot.components.Buttons;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

@Data
@AllArgsConstructor
public class SubCommandInfo {
    private String startMessage;
    private Runnable parent;
    private String errorMessage;
    private String nextSumCommand;
    private String regex;
    private Runnable handler;
    private ReplyKeyboard keyboard;

    public SubCommandInfo(String startMessage, String errorMessage, Runnable parent, String nextSumCommand, String regex, Runnable handler) {
        this(startMessage, errorMessage, parent, nextSumCommand);
        this.regex = regex;
        this.handler = handler;
    }

    public SubCommandInfo(String startMessage, String errorMessage, Runnable parent, String nextSumCommand) {
        this(parent);
        this.startMessage = startMessage;
        this.errorMessage = errorMessage;
        this.nextSumCommand = nextSumCommand;
    }

    public SubCommandInfo(Runnable parent) {
        this.parent = parent;
        this.keyboard = Buttons.keyboardMarkupCommands();
    }
}
