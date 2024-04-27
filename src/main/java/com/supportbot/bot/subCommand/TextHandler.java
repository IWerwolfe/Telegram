package com.supportbot.bot.subCommand;

import com.supportbot.bot.SupportBot;
import com.supportbot.model.command.CommandCache;
import com.supportbot.services.SenderService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@RequiredArgsConstructor
public abstract class TextHandler implements Procedure {

    private final SenderService senderService;

    @Override
    public abstract String getStartMessage();

    @Override
    public abstract String getErrorMessage();

    @Override
    public boolean execute(SupportBot supportBot, CommandCache current, String text) {

        if (current.getAndIncrement() == 0) {
            senderService.sendBotMessage(supportBot, getStartMessage(), current.getUserBD().getId());
            return false;
        }

        current.increment();

        if (text.matches(".{10,}")) {
            current.setResult(text);
            current.setComplete(true);
            return true;
        }

        senderService.sendBotMessage(supportBot, getErrorMessage(), current.getUserBD().getId());
        return false;
    }
}
