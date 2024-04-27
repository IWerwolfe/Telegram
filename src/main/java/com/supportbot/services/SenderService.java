package com.supportbot.services;

import com.supportbot.bot.SupportBot;
import com.supportbot.model.transaction.TransactionType;
import com.supportbot.model.user.UserBD;
import com.supportbot.utils.TextUtils;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodBoolean;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.methods.invoices.SendInvoice;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.Serializable;

@Slf4j
@Component
@RequiredArgsConstructor
@Setter
public class SenderService {

    private final TextUtils textUtils;

    public void sendBotMessage(SupportBot absSender, SendMessage message) {
        int id = sendMessage(absSender, message);
    }

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

    public void sendBotMessage(SupportBot absSender, String message, ReplyKeyboard keyboard, long chatId) {
        SendMessage sendMessage = createMessage(chatId, message, keyboard);
        sendBotMessage(absSender, sendMessage);
    }

    public void sendBotMessage(SupportBot absSender, String message, long chatId) {
        SendMessage sendMessage = createMessage(chatId, message, null);
        sendBotMessage(absSender, sendMessage);
    }

    public void sendBotMessage(SupportBot absSender, String message, ReplyKeyboard keyboard, long chatId, UserBD user) {
        SendMessage sendMessage = createMessage(chatId, message, keyboard);
        sendBotMessage(absSender, sendMessage, user);
    }

    public void sendBotEditMessage(SupportBot absSender, String message, InlineKeyboardMarkup keyboard, long chatId, int messageId) {
        BotApiMethod<? extends Serializable> sendMessage = messageId == 0 ?
                createMessage(chatId, message, keyboard) : createEditMessage(chatId, message, keyboard, messageId);
        sendMessage(absSender, sendMessage);
    }

    private EditMessageText createEditMessage(long chatId, String text, InlineKeyboardMarkup keyboard, int messageId) {

        EditMessageText message = new EditMessageText();
        message.setChatId(chatId);
        message.setText(textUtils.shortenText(text, 4095));
        message.setMessageId(messageId);
        message.setReplyMarkup(keyboard);
        return message;
    }

    private SendMessage createMessage(long chatId, String text, ReplyKeyboard keyboard) {

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(textUtils.shortenText(text, 4095));
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
