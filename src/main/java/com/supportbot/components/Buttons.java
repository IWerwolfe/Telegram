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
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class Buttons {

    private static final int SIZE_INLINE_BUTTON = 64;
    private static final KeyboardButton NEED_HELP = new KeyboardButton("Новое обращение");
    private static final KeyboardButton GET_TASKS = new KeyboardButton("Активные");
    private static final KeyboardButton CREATE_TASK = new KeyboardButton("Новое обращение");
    private static final KeyboardButton GET_BALANCE = new KeyboardButton("Проверить баланс");
    private static final KeyboardButton SEND_CONTACT = new KeyboardButton("Зарегистрироваться");
    private static final KeyboardButton ADD_BALANCE = new KeyboardButton("Пополнить баланс");
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

    static {

        BUY.setCallbackData("/buy");
        START_BUTTON.setCallbackData("/start");
        HELP_BUTTON.setCallbackData("/help");
    }

    private final PaySetting paySetting;

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

    private ReplyKeyboardMarkup createReplyKeyboardMarkup(List<KeyboardRow> rows) {
        return createReplyKeyboardMarkup(rows, true);
    }

    public InlineKeyboardMarkup getInlineByEnumFormOfPay(String command) {

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        if (paySetting.isCard()) {
            rows.add(getInlineKeyboardButtons(FormOfPayment.CARD.getLabel(), command, FormOfPayment.CARD.name()));
        }

        if (paySetting.isBank()) {
            rows.add(getInlineKeyboardButtons(FormOfPayment.INVOICE.getLabel(), command, FormOfPayment.INVOICE.name()));
        }

        if (paySetting.isSBPStatic()) {
            rows.add(getInlineKeyboardButtons(FormOfPayment.SBP_STATIC.getLabel(), command, FormOfPayment.SBP_STATIC.name()));
        }

        if (paySetting.isSBP()) {
            rows.add(getInlineKeyboardButtons(FormOfPayment.SBP.getLabel(), command, FormOfPayment.SBP.name()));
        }

        if (paySetting.isCrypto()) {
            rows.add(getInlineKeyboardButtons(FormOfPayment.CRYPTO.getLabel(), command, FormOfPayment.CRYPTO.name()));
        }

        return getInlineKeyboardMarkup(rows);
    }

    public InlineKeyboardMarkup getInlineConfirmConsent(String command) {

        InlineKeyboardButton yes = getInlineKeyboardButton("Да", command, "yes");
        InlineKeyboardButton no = getInlineKeyboardButton("Нет", command, "no");

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        rows.add(List.of(yes, no));

        return getInlineKeyboardMarkup(rows);
    }

    public <E extends Reference> InlineKeyboardMarkup getInlineByRef(String command, List<E> list, boolean addEmpty) {

        List<List<InlineKeyboardButton>> rowsInLine = list.stream()
                .map(field -> getInlineKeyboardButtons(field, command))
                .collect(Collectors.toList());

        if (addEmpty) {
            rowsInLine.add(getInlineKeyboardButtons("Пропустить", command, ""));
        }

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
                .map(taskDoc ->
                        getInlineKeyboardButtons(
                                getDescriptorToInline(taskDoc),
                                "getTask",
                                String.valueOf(taskDoc.getId())))
                .toList();
        return getInlineKeyboardMarkup(rowsInLine);
    }

    private List<InlineKeyboardButton> getInlineKeyboardButtons(String label, String command) {
        return List.of(getInlineKeyboardButton(label, command));
    }

    private List<InlineKeyboardButton> getInlineKeyboardButtons(String label, String commandName, String result) {
        return List.of(getInlineKeyboardButton(label, commandName, result));
    }

    private List<InlineKeyboardButton> getInlineKeyboardButtons(Reference ref, String command) {
        return getInlineKeyboardButtons(ref.getName(), command, String.valueOf(ref.getId()));
    }

    private InlineKeyboardButton getInlineKeyboardButton(String label, String command) {
        InlineKeyboardButton taskButton = new InlineKeyboardButton(label);
        taskButton.setCallbackData(command);
        return taskButton;
    }

    private InlineKeyboardButton getInlineKeyboardButton(String label, String commandName, String result) {
        InlineKeyboardButton taskButton = new InlineKeyboardButton(label);
        taskButton.setCallbackData(commandName + ":" + result);
        return taskButton;
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
