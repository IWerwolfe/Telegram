package com.telegrambot.app.services;

import com.telegrambot.app.components.Buttons;
import com.telegrambot.app.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
@Slf4j
public class BotCommandsImpl implements com.telegrambot.app.components.BotCommands {

    private final TelegramBotServices parent;
    private final String separator = System.lineSeparator();
    private final String dualSeparator = System.lineSeparator() + System.lineSeparator();

    public BotCommandsImpl(TelegramBotServices parent) {
        this.parent = parent;
    }

    public void botAnswerUtils(String receivedMessage, long chatId, User user) {
        switch (receivedMessage) {
            case "/start" -> startBot(chatId, user);
            case "/help" -> sendHelpText(chatId, HELP_TEXT);
            case "/send_contact" -> getContact(chatId, user);
            case "/afterRegistered" -> afterRegistered(chatId, user);
            default -> sendDefault(chatId, user.getFirstName());
        }
    }

    private void afterRegistered(long chatId, User user) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Ваш статус успешно обновлен, всего хорошего. Ваш номер: " + user.getPhone());
//        message.setReplyMarkup(Buttons.getContact());
        parent.sentMassage(message);
    }


    private void getContact(long chatId, User user) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Для получения персонализированной поддержки и доступа к информации, пожалуйста, нажмите на кнопку ниже. " +
                "Мы гарантируем конфиденциальность и безопасность ваших данных. " + dualSeparator +
                "После отправки номера телефона, наша система проведет поиск информации и произведет регистрацию. " +
                "Затем мы присвоим вам соответствующий статус и предоставим вам точную и полезную информацию по вашему предприятию." +
                dualSeparator + "Мы ценим ваше доверие и стремимся обеспечить наивысший уровень сервиса.");
        message.setReplyMarkup(Buttons.getContact());
        parent.sentMassage(message);
    }

    private void startBot(long chatId, User user) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Добро пожаловать в наш телеграмм бот!" + dualSeparator +
                "Устранение проблем с ПО и кассовым оборудованием - наша специализация. " +
                "Мы готовы предложить вам быстрые и профессиональные решения. " + separator +
                "Оставьте заявку, и наша команда опытных специалистов поможет вам без лишних хлопот. " + dualSeparator +
                "Доверьтесь нам, и ваш бизнес станет еще успешнее!");
        message.setReplyMarkup(Buttons.inlineMarkup());
        parent.sentMassage(message);
    }

    private void sendHelpText(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(textToSend);
        parent.sentMassage(message);
    }

    private void sendDefault(long chatId, String name) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(name + ", мы не смогли обработать вашу команду, приносим свои извинения");
        parent.sentMassage(message);
    }
}
