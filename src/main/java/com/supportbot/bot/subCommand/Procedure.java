package com.supportbot.bot.subCommand;

import com.supportbot.bot.SupportBot;
import com.supportbot.model.command.CommandCache;

public interface Procedure {

    String getStartMessage();

    String getErrorMessage();

    default String getDescription() {
        return this.getClass().getSimpleName();
    }

    ;

    boolean execute(SupportBot supportBot, CommandCache current, String text);
}
