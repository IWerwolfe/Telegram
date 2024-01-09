package com.telegrambot.app.bot.command;

import com.telegrambot.app.services.SenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Service
@Slf4j
@RequiredArgsConstructor
public class StartCommand implements IBotCommand {

    private final SenderService senderService;

    @Override
    public String getCommandIdentifier() {
        return "start";
    }

    @Override
    public String getDescription() {
        return "Начало работы с ботом";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {

//        String text = weatherService.getDescriptorCurrentWeather();
//
//        SendMessage sendMessage = new SendMessage();
//        sendMessage.setChatId(message.getChatId());
//        sendMessage.setText(text);
//
//        senderService.sendMessage(absSender, sendMessage, getCommandIdentifier());
    }

}
