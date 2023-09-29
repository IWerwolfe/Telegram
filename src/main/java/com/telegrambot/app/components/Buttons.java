package com.telegrambot.app.components;    /*
 *created by WerWolfe on Buttons
 */

import com.telegrambot.app.DTO.types.FormOfPayment;
import com.telegrambot.app.model.documents.doc.service.TaskDoc;
import com.telegrambot.app.model.reference.TaskStatus;
import com.telegrambot.app.model.types.Reference;
import com.telegrambot.app.model.user.UserType;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.*;
import java.util.stream.Collectors;

public class Buttons {

    private static final int SIZE_INLINE_BUTTON = 64;
    private static final InlineKeyboardButton START_BUTTON = new InlineKeyboardButton("Start");
    private static final InlineKeyboardButton HELP_BUTTON = new InlineKeyboardButton("Справка");
    private static final InlineKeyboardButton NEED_HELP = new InlineKeyboardButton("Создать обращение");
    private static final InlineKeyboardButton BUY = new InlineKeyboardButton("Купить");
    private static final InlineKeyboardButton CANCEL_TASK = new InlineKeyboardButton("Отменить");
    private static final InlineKeyboardButton CLOSE_TASK = new InlineKeyboardButton("Закрыть");
    private static final InlineKeyboardButton EDIT_TASK = new InlineKeyboardButton("Редактировать");
    private static final InlineKeyboardButton PAY_TASK = new InlineKeyboardButton("Оплатить");
    private static final InlineKeyboardButton EDIT_COMMENT_TASK = new InlineKeyboardButton("Редактировать комментарий");
    private static final InlineKeyboardButton EDIT_DESCRIPTION_TASK = new InlineKeyboardButton("Редактировать описание");
    private static final InlineKeyboardButton GET_TASKS = new InlineKeyboardButton("Посмотреть задачи");
    private static final InlineKeyboardButton GET_BALANCE = new InlineKeyboardButton("Проверить баланс");
    private static final InlineKeyboardButton CREATE_TASK = new InlineKeyboardButton("Создать задачу");
    private static final InlineKeyboardButton SEND_CONTACT = new InlineKeyboardButton("Зарегистрироваться");
    private static final InlineKeyboardButton ADD_BALANCE = new InlineKeyboardButton("Пополнить баланс");
    private static final KeyboardButton GET_CONTACT = new KeyboardButton("Отправить номер телефона");

    public static void init() {
        GET_TASKS.setCallbackData("/get_task");
        GET_BALANCE.setCallbackData("/get_balance");
        CREATE_TASK.setCallbackData("/create_task");
        BUY.setCallbackData("/buy");
        ADD_BALANCE.setCallbackData("/add_balance");
        NEED_HELP.setCallbackData("/need_help");
        SEND_CONTACT.setCallbackData("/send_contact");
        START_BUTTON.setCallbackData("/start");
        HELP_BUTTON.setCallbackData("/help");
    }

    public static InlineKeyboardMarkup inlineMarkupDefault(UserType userType) {

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        switch (userType) {
            case USER -> {
                rows.add(List.of(BUY, CREATE_TASK));
                rows.add(List.of(GET_TASKS, GET_BALANCE));
                rows.add(List.of(ADD_BALANCE));
            }
            case ADMINISTRATOR -> {
                rows.add(List.of(BUY, CREATE_TASK));
                rows.add(List.of(GET_TASKS));
                rows.add(List.of(GET_BALANCE, ADD_BALANCE));
            }
            case DIRECTOR -> {
                rows.add(List.of(BUY, CREATE_TASK));
                rows.add(List.of(GET_TASKS, GET_BALANCE));
            }
            default -> {
                rows.add(List.of(BUY, NEED_HELP));
                rows.add(List.of(GET_TASKS, SEND_CONTACT));
                rows.add(List.of(ADD_BALANCE, GET_BALANCE));
            }
        }

        return getInlineKeyboardMarkup(rows);
    }

    public static InlineKeyboardMarkup getInlineByEnumFormOfPay(String command) {
        List<List<InlineKeyboardButton>> rowsInLine = Arrays.stream(FormOfPayment.values())
                .map(field -> getInlineKeyboardButton(field.name(), command + ":" + field))
                .collect(Collectors.toList());
        return getInlineKeyboardMarkup(rowsInLine);
    }

    public static <E extends Reference> InlineKeyboardMarkup getInlineByRef(String command, List<E> list) {
        List<List<InlineKeyboardButton>> rowsInLine = list.stream()
                .map(field -> getInlineKeyboardButton(field, command))
                .collect(Collectors.toList());
        return getInlineKeyboardMarkup(rowsInLine);
    }

    public static InlineKeyboardMarkup inlineMarkup() {
        List<InlineKeyboardButton> rowInline = List.of(START_BUTTON, HELP_BUTTON, SEND_CONTACT);
        List<List<InlineKeyboardButton>> rowsInLine = List.of(rowInline);
        return getInlineKeyboardMarkup(rowsInLine);
    }

    public static InlineKeyboardMarkup getInlineMarkupEditTask(TaskDoc taskDoc) {

        PAY_TASK.setCallbackData("pay:" + taskDoc.getId());
        PAY_TASK.setPay(true);

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        boolean isPay = taskDoc.getTotalAmount() != null && taskDoc.getTotalAmount() > 0;
        boolean isClosed = Objects.equals(taskDoc.getStatus().getId(), TaskStatus.getDefaultClosedStatus().getId());

        if (isPay && isClosed) {
            rows.add(List.of(PAY_TASK));
            return getInlineKeyboardMarkup(rows);
        }

        CANCEL_TASK.setCallbackData("cancel:" + taskDoc.getId());
        EDIT_COMMENT_TASK.setCallbackData("comment:" + taskDoc.getId());
        EDIT_DESCRIPTION_TASK.setCallbackData("descriptor:" + taskDoc.getId());

        rows.add(List.of(EDIT_DESCRIPTION_TASK));
        rows.add(List.of(EDIT_COMMENT_TASK));
        rows.add(isPay ? List.of(CANCEL_TASK, PAY_TASK) : List.of(CANCEL_TASK));

        return getInlineKeyboardMarkup(rows);
    }

    public static InlineKeyboardMarkup getInlineMarkupByTasks(List<TaskDoc> taskDocs) {
        List<List<InlineKeyboardButton>> rowsInLine = taskDocs.stream()
                .map(taskDoc -> {
                    String name = getDescriptorToInline(taskDoc);
                    String command = "getTask:" + taskDoc.getId();
                    return getInlineKeyboardButton(name, command);
                })
                .toList();
        return getInlineKeyboardMarkup(rowsInLine);
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

    private static List<InlineKeyboardButton> getInlineKeyboardButton(String label, String command) {
        InlineKeyboardButton taskButton = new InlineKeyboardButton(label);
        taskButton.setCallbackData(command);
        return List.of(taskButton);
    }

    private static List<InlineKeyboardButton> getInlineKeyboardButton(Reference ref, String command) {
        return getInlineKeyboardButton(ref.getName(), command + ":" + ref.getId());
    }

    public static String getDescriptorToInline(TaskDoc taskDoc) {
        String description = convertCode(taskDoc.getCodeEntity()) + " > " + convertDescription(taskDoc.getDescription());
        return description.length() > SIZE_INLINE_BUTTON ?
                description.substring(0, SIZE_INLINE_BUTTON - 18) :
                String.format("%-" + SIZE_INLINE_BUTTON + "s", description);
    }

    public static String convertDescription(String code) {
        String REGEX = "[^0-9a-zA-Zа-яА-ЯёЁ\\-.,=_*+&:#№@!/(){}\\[\\]]+";
        return code.replaceAll(REGEX, " ").replaceAll("\s{2,}", " ").trim();
    }

    public static String convertCode(String code) {
        String REGEX_CODE = "^0+";
        return code.replaceAll(REGEX_CODE, "").trim();
    }

    private static InlineKeyboardMarkup getInlineKeyboardMarkup(List<List<InlineKeyboardButton>> rows) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rows);
        return markupInline;
    }
}
