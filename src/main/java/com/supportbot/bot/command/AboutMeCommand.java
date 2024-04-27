package com.supportbot.bot.command;

import com.supportbot.bot.SupportBot;
import com.supportbot.services.SenderService;
import com.supportbot.services.UserBotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Service
@Slf4j
@RequiredArgsConstructor
public class AboutMeCommand implements IBotCommand {

    private final SenderService senderService;
    private final UserBotService userBotService;

    @Override
    public String getCommandIdentifier() {
        return "about_me";
    }

    @Override
    public String getDescription() {
        return "Получить информацию о себе";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {
        String text = userBotService.getUser(message.getFrom()).getNamePresentation();
        senderService.sendBotMessage((SupportBot) absSender, text, message.getChatId());
    }

}
