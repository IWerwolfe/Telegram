package com.supportbot.services;

import com.supportbot.bot.SupportBot;
import com.supportbot.model.transaction.TransactionType;
import com.supportbot.model.user.UserBD;
import com.supportbot.repositories.transaction.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodBoolean;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.methods.invoices.SendInvoice;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.Serializable;

@Slf4j
@Component
@RequiredArgsConstructor
@Setter
public class SenderService {

    private final TransactionRepository transactionRepository;

    public void sendBotMessage(SupportBot absSender, SendMessage message, UserBD user) {
        int id = sendMessage(absSender, message);
        absSender.saveTransaction(message.getText(), id, TransactionType.BOT_MESSAGE, user);
    }

    public void sendBotMessage(SupportBot absSender, SendInvoice message, UserBD user) {
        int id = sendMessage(absSender, message);
        absSender.saveTransaction(message.getTitle(), id, TransactionType.MESSAGE, user);
    }

    public void sendErrorMessage(SupportBot absSender, SendMessage message, UserBD user) {
        int id = sendMessage(absSender, message, true);
        absSender.saveTransaction(message.getText(), id, TransactionType.ERROR_NOTIFY, user);
    }

    public void sendNotifyMessage(SupportBot absSender, SendMessage message, UserBD user) {
        int id = sendMessage(absSender, message);
        absSender.saveTransaction(message.getText(), id, TransactionType.NOTIFY, user);
    }

    public void sendPreCheckMessage(SupportBot absSender, BotApiMethodBoolean message, UserBD user) {
        sendMessage(absSender, message);
        absSender.saveTransaction("Подтверждение оплаты", 0, TransactionType.BOT_MESSAGE, user);
    }

    public void sendBotMessage(SupportBot absSender, String message, ReplyKeyboard keyboard, long chatId, UserBD user) {

        if (message != null && message.trim().isEmpty()) {
            log.error("Attempt to send an empty message from a user with an ID {}", chatId);
            return;
        }
        SendMessage sendMessage = createMessage(chatId, message, keyboard);
        sendBotMessage(absSender, sendMessage, user);
    }

    private SendMessage createMessage(long chatId, String text, ReplyKeyboard keyboard) {

        if (text.length() > 4095) {
            text = text.substring(0, 4092) + "...";
        }

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        message.setReplyMarkup(keyboard);
        return message;
    }

    private <T extends Serializable> int sendMessage(SupportBot absSender, BotApiMethod<T> message) {
        return sendMessage(absSender, message, false);
    }

    private <T extends Serializable> int sendMessage(SupportBot absSender, BotApiMethod<T> message, boolean notHandleError) {
        try {

            if (message instanceof BotApiMethodMessage method) {
                return absSender.execute(method).getMessageId();
            }
            if (message instanceof BotApiMethodBoolean method) {
                absSender.execute(method);
            }

            return 0;

        } catch (TelegramApiException e) {

            if (notHandleError) {
                log.error(e.getMessage());
            } else {
                absSender.handleAnException(e.getMessage());
            }

            return -1;
        }
    }
}
