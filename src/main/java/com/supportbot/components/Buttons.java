package com.supportbot.components;    /*
 *created by WerWolfe on Buttons
 */

import com.supportbot.DTO.types.FormOfPayment;
import com.supportbot.config.PaySetting;
import com.supportbot.model.documents.doc.service.TaskDoc;
import com.supportbot.model.reference.TaskStatus;
import com.supportbot.model.types.Reference;
import com.supportbot.model.user.UserType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class Buttons {

    private final PaySetting paySetting;

    private static final int SIZE_INLINE_BUTTON = 64;

    private static final KeyboardButton NEED_HELP = new KeyboardButton("Создать обращение");
    private static final KeyboardButton GET_TASKS = new KeyboardButton("Посмотреть задачи");
    private static final KeyboardButton CREATE_TASK = new KeyboardButton("Создать задачу");
    private static final KeyboardButton GET_BALANCE = new KeyboardButton("Проверить баланс");
    private static final KeyboardButton SEND_CONTACT = new KeyboardButton("Зарегистрироваться");
    private static final KeyboardButton ADD_BALANCE = new KeyboardButton("Пополнить баланс");
    private static final KeyboardButton GET_CONTACT = new KeyboardButton("Отправить номер телефона");
    private static final KeyboardButton EXIT = new KeyboardButton("Отмена");


    private static final InlineKeyboardButton START_BUTTON = new InlineKeyboardButton("Start");
    private static final InlineKeyboardButton HELP_BUTTON = new InlineKeyboardButton("Справка");
    private static final InlineKeyboardButton BUY = new InlineKeyboardButton("Купить");
    private static final InlineKeyboardButton CANCEL_TASK = new InlineKeyboardButton("Отменить");
    private static final InlineKeyboardButton CLOSE_TASK = new InlineKeyboardButton("Закрыть");
    private static final InlineKeyboardButton EDIT_TASK = new InlineKeyboardButton("Редактировать");
    private static final InlineKeyboardButton PAY_TASK = new InlineKeyboardButton("Оплатить");
    private static final InlineKeyboardButton EDIT_COMMENT_TASK = new InlineKeyboardButton("Редактировать комментарий");
    private static final InlineKeyboardButton EDIT_DESCRIPTION_TASK = new InlineKeyboardButton("Редактировать описание");


    public static void init() {

        BUY.setCallbackData("/buy");
        START_BUTTON.setCallbackData("/start");
        HELP_BUTTON.setCallbackData("/help");
    }

    public ReplyKeyboardMarkup keyboardMarkupDefault(UserType userType) {

        List<KeyboardRow> rows = new ArrayList<>();

        switch (userType) {
            case USER, ADMINISTRATOR, DIRECTOR -> {
                rows.add(new KeyboardRow(List.of(GET_TASKS, CREATE_TASK)));
                addPayButton(rows);
            }
            default -> {
                SEND_CONTACT.setRequestContact(true);
                rows.add(new KeyboardRow(List.of(GET_TASKS, NEED_HELP)));
                addPayButton(rows);
                rows.add(new KeyboardRow(List.of(SEND_CONTACT)));
            }
        }

        return createReplyKeyboardMarkup(rows);
    }

    private void addPayButton(List<KeyboardRow> rows) {

        if (!paySetting.isUse()) {
            return;
        }

        KeyboardRow rowsPay = new KeyboardRow();

        if (paySetting.isBalanceVisibly()) {
            rowsPay.add(GET_BALANCE);
        }

        if (paySetting.isAddBalance()) {
            rowsPay.add(ADD_BALANCE);
        }

        if (!rowsPay.isEmpty()) {
            rows.add(rowsPay);
        }
    }

    public static ReplyKeyboardMarkup keyboardMarkupCommands() {
        KeyboardRow row = new KeyboardRow();
        row.add(EXIT);
        return createReplyKeyboardMarkup(List.of(row), false);
    }

    private static ReplyKeyboardMarkup createReplyKeyboardMarkup(List<KeyboardRow> rows, boolean oneTimeKeyboard) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(oneTimeKeyboard);
        keyboardMarkup.setKeyboard(rows);
        return keyboardMarkup;
    }

    private ReplyKeyboardMarkup createReplyKeyboardMarkup(List<KeyboardRow> rows) {
        return createReplyKeyboardMarkup(rows, true);
    }

    public InlineKeyboardMarkup getInlineByEnumFormOfPay(String command) {

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        if (paySetting.isCard()) {
            rows.add(getInlineKeyboardButton(FormOfPayment.CARD.getLabel(), command + ":" + FormOfPayment.CARD.name()));
        }

        if (paySetting.isBank()) {
            rows.add(getInlineKeyboardButton(FormOfPayment.INVOICE.getLabel(), command + ":" + FormOfPayment.INVOICE.name()));
        }

        if (paySetting.isSBPStatic()) {
            rows.add(getInlineKeyboardButton(FormOfPayment.SBP_STATIC.getLabel(), command + ":" + FormOfPayment.SBP_STATIC.name()));
        }

        if (paySetting.isSBP()) {
            rows.add(getInlineKeyboardButton(FormOfPayment.SBP.getLabel(), command + ":" + FormOfPayment.SBP.name()));
        }

        if (paySetting.isCrypto()) {
            rows.add(getInlineKeyboardButton(FormOfPayment.CRYPTO.getLabel(), command + ":" + FormOfPayment.CRYPTO.name()));
        }

        return getInlineKeyboardMarkup(rows);
    }

    public <E extends Reference> InlineKeyboardMarkup getInlineByRef(String command, List<E> list) {
        List<List<InlineKeyboardButton>> rowsInLine = list.stream()
                .map(field -> getInlineKeyboardButton(field, command))
                .collect(Collectors.toList());
        return getInlineKeyboardMarkup(rowsInLine);
    }

    public InlineKeyboardMarkup getInlineMarkupEditTask(TaskDoc taskDoc) {

        PAY_TASK.setCallbackData("pay:" + taskDoc.getId());
        PAY_TASK.setPay(true);

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        boolean isPay = taskDoc.getTotalAmount() != null && taskDoc.getTotalAmount() > 0 && paySetting.isUse();
        boolean isClosed = Objects.equals(taskDoc.getStatus().getId(), TaskStatus.getClosedStatus().getId());

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

    public InlineKeyboardMarkup getInlineMarkupByTasks(List<TaskDoc> taskDocs) {
        List<List<InlineKeyboardButton>> rowsInLine = taskDocs.stream()
                .map(taskDoc -> {
                    String name = getDescriptorToInline(taskDoc);
                    String command = "getTask:" + taskDoc.getId();
                    return getInlineKeyboardButton(name, command);
                })
                .toList();
        return getInlineKeyboardMarkup(rowsInLine);
    }

    public ReplyKeyboardMarkup getContact() {
        GET_CONTACT.setRequestContact(true);
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true);
        KeyboardRow row = new KeyboardRow();
        row.add(GET_CONTACT);
        keyboardMarkup.setKeyboard(Collections.singletonList(row));
        return keyboardMarkup;
    }

    private List<InlineKeyboardButton> getInlineKeyboardButton(String label, String command) {
        InlineKeyboardButton taskButton = new InlineKeyboardButton(label);
        taskButton.setCallbackData(command);
        return List.of(taskButton);
    }

    private List<InlineKeyboardButton> getInlineKeyboardButton(Reference ref, String command) {
        return getInlineKeyboardButton(ref.getName(), command + ":" + ref.getId());
    }

    public String getDescriptorToInline(TaskDoc taskDoc) {
        String description = convertCode(taskDoc.getCodeEntity()) + " > " + convertDescription(taskDoc.getDescription());
        return description.length() > SIZE_INLINE_BUTTON ?
                description.substring(0, SIZE_INLINE_BUTTON - 18) :
                String.format("%-" + SIZE_INLINE_BUTTON + "s", description);
    }

    public String convertDescription(String code) {
        String REGEX = "[^0-9a-zA-Zа-яА-ЯёЁ\\-.,=_*+&:#№@!/(){}\\[\\]]+";
        return code.replaceAll(REGEX, " ").replaceAll("\s{2,}", " ").trim();
    }

    public String convertCode(String code) {
        String REGEX_CODE = "^0+";
        return code.replaceAll(REGEX_CODE, "").trim();
    }

    private InlineKeyboardMarkup getInlineKeyboardMarkup(List<List<InlineKeyboardButton>> rows) {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rows);
        return markupInline;
    }
}
