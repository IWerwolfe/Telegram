package com.telegrambot.app.components;    /*
 *created by WerWolfe on Buttons
 */

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.Collections;
import java.util.List;

public class Buttons {
    private static final InlineKeyboardButton START_BUTTON = new InlineKeyboardButton("Start");
    private static final InlineKeyboardButton HELP_BUTTON = new InlineKeyboardButton("Help");
    private static final InlineKeyboardButton SEND_CONTACT = new InlineKeyboardButton("Registered");
    private static final KeyboardButton GET_CONTACT = new KeyboardButton("Send you phone");


    public static InlineKeyboardMarkup inlineMarkup() {
        START_BUTTON.setCallbackData("/start");
        HELP_BUTTON.setCallbackData("/help");
        SEND_CONTACT.setCallbackData("/send_contact");

        List<InlineKeyboardButton> rowInline = List.of(START_BUTTON, HELP_BUTTON, SEND_CONTACT);
        List<List<InlineKeyboardButton>> rowsInLine = List.of(rowInline);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rowsInLine);

        return markupInline;
    }

    public static ReplyKeyboardMarkup getContact() {
        GET_CONTACT.setRequestContact(true);
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true);
        KeyboardRow row = new KeyboardRow();
        row.add(GET_CONTACT);
        keyboardMarkup.setKeyboard(Collections.singletonList(row));
        return keyboardMarkup;
    }
}
