package com.telegrambot.app.components;    /*
 *created by WerWolfe on Buttons
 */

import com.telegrambot.app.model.task.Task;
import com.telegrambot.app.model.user.UserType;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Buttons {
    private static final InlineKeyboardButton START_BUTTON = new InlineKeyboardButton("Start");
    private static final InlineKeyboardButton HELP_BUTTON = new InlineKeyboardButton("Справка");
    private static final InlineKeyboardButton NEED_HELP = new InlineKeyboardButton("Нужна помощь");
    private static final InlineKeyboardButton BUY = new InlineKeyboardButton("Купить");
    private static final InlineKeyboardButton CLOSE_TASK = new InlineKeyboardButton("Закрыть задачу");
    private static final InlineKeyboardButton EDIT_TASK = new InlineKeyboardButton("Редактировать задачу");
    private static final InlineKeyboardButton PAY_TASK = new InlineKeyboardButton("Оплатить задачу");
    private static final InlineKeyboardButton EDIT_COMMENT_TASK = new InlineKeyboardButton("Редактировать комментарий");
    private static final InlineKeyboardButton EDIT_DESCRIPTION_TASK = new InlineKeyboardButton("Редактировать описание");
    private static final InlineKeyboardButton GET_TASKS = new InlineKeyboardButton("Посмотреть задачи");
    private static final InlineKeyboardButton GET_BALANCE = new InlineKeyboardButton("Проверить баланс");
    private static final InlineKeyboardButton CREATE_TASK = new InlineKeyboardButton("Создать задачу");
    private static final InlineKeyboardButton SEND_CONTACT = new InlineKeyboardButton("Зарегистрироваться");
    private static final KeyboardButton GET_CONTACT = new KeyboardButton("Отправить номер телефона");

    public static InlineKeyboardMarkup inlineMarkupDefault(UserType userType) {

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        switch (userType) {

            case USER -> {
                GET_TASKS.setCallbackData("/get_task");
                GET_BALANCE.setCallbackData("/get_balance");
                CREATE_TASK.setCallbackData("/create_task");
                BUY.setCallbackData("/buy");
                List<InlineKeyboardButton> row1Line = List.of(BUY, CREATE_TASK);
                List<InlineKeyboardButton> row2Line = List.of(GET_TASKS, GET_BALANCE);
                rows.add(row1Line);
                rows.add(row2Line);
            }
            case ADMINISTRATOR -> {
            }
            case DIRECTOR -> {
                GET_TASKS.setCallbackData("/get_task");
                GET_BALANCE.setCallbackData("/get_balance");
                CREATE_TASK.setCallbackData("/create_task");
                BUY.setCallbackData("/buy");
                List<InlineKeyboardButton> row1Line = List.of(BUY, CREATE_TASK);
                List<InlineKeyboardButton> row2Line = List.of(GET_TASKS, GET_BALANCE);
                rows.add(row1Line);
                rows.add(row2Line);
            }
            default -> {
                BUY.setCallbackData("/buy");
                NEED_HELP.setCallbackData("/need_help");
                SEND_CONTACT.setCallbackData("/send_contact");
                List<InlineKeyboardButton> row1Line = List.of(BUY, NEED_HELP);
                List<InlineKeyboardButton> row2Line = List.of(SEND_CONTACT);
                rows.add(row1Line);
                rows.add(row2Line);
            }
        }

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rows);

        return markupInline;
    }


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

    public static InlineKeyboardMarkup getInlineMarkupByTask(List<Task> tasks) {

        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();

        for (Task task : tasks) {
            String description = convertCode(task.getCode()) + " > " + convertDescription(task.getDescription());
            InlineKeyboardButton taskButton = new InlineKeyboardButton(description);
            taskButton.setCallbackData("getTask:" + task.getCode());

            List<InlineKeyboardButton> rowInline = List.of(taskButton);
            rowsInLine.add(rowInline);
        }

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

    public static String convertDescription(String code) {
        String REGEX = "[^0-9a-zA-Zа-яА-ЯёЁ\\-.,=_*+&:#№@!/(){}\\[\\]]+";
        return code.replaceAll(REGEX, " ").replaceAll("\s{2,}", " ").trim();
    }

    public static String convertCode(String code) {
        String REGEX_CODE = "^0+";
        return code.replaceAll(REGEX_CODE, "").trim();
    }
}
