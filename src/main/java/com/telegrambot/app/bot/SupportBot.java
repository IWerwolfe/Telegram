package com.telegrambot.app.bot;

import com.telegrambot.app.config.BotConfig;
import com.telegrambot.app.config.BugNotifications;
import com.telegrambot.app.model.command.CommandCache;
import com.telegrambot.app.model.transaction.Transaction;
import com.telegrambot.app.model.transaction.TransactionType;
import com.telegrambot.app.model.user.UserActivity;
import com.telegrambot.app.model.user.UserBD;
import com.telegrambot.app.repositories.command.CommandCacheRepository;
import com.telegrambot.app.repositories.transaction.TransactionRepository;
import com.telegrambot.app.repositories.user.UserActivityRepository;
import com.telegrambot.app.repositories.user.UserRepository;
import com.telegrambot.app.services.BotCommandsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerPreCheckoutQuery;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodBoolean;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.methods.invoices.SendInvoice;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.payments.PreCheckoutQuery;
import org.telegram.telegrambots.meta.api.objects.payments.SuccessfulPayment;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SupportBot extends TelegramLongPollingBot {

    private final BotConfig botConfig;
    private final BugNotifications bugNotifications;

    private final BotCommandsImpl botCommands;
    private final UserActivityRepository activityRepository;
    private final CommandCacheRepository commandCacheRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private Update update;
    private UserBD user;
    private boolean isCommand = false;

    public void onUpdateReceived(Update update) {

        this.update = update;
        this.user = getUser();
        botCommands.setParent(this);

        updateLastActivity();

        if (update.hasMyChatMember()) {

            String newStatus = update.getMyChatMember().getNewChatMember().getStatus();

            this.user.setNotValid(newStatus.equals("kicked"));
            userRepository.save(this.user);

            String text = "У пользователя с id: " + getUserID() + " изменился статус на " + newStatus;
            saveTransaction(text);
            log.warn(text);
            return;
        }

        boolean isGroupMessage = update.hasMessage() &&
                update.getMessage().getChat() != null &&
                !update.getMessage().getChat().getType().equals("private");

        if (this.user == null) {
            String text = com.telegrambot.app.DTO.message.Message.getSkippingMessageFromUnknownUser(getReceivedMessage());
            saveTransaction(text);
            log.warn(text);
            return;
        }

        if (isGroupMessage) {
            saveTransaction(com.telegrambot.app.DTO.message.Message.getSkippingMessageFromGroup());
            return;
        }

        try {
            handlerMessage(update);
            saveTransaction();
        } catch (Exception e) {
            handleAnException("Ошибка при обработке сообщения id: %s, текст: \"%s\" \r\n от user ID: %s \r\n %s",
                    String.valueOf(getMessageId()),
                    getReceivedMessage(),
                    getUserID(),
                    e.getMessage());
        }
    }

    private void handlerMessage(Update update) throws Exception {
        if (isBot()) {
            SendMessage sendMessage = new SendMessage(String.valueOf(getChatId()), getReceivedMessage());
            sendMessage(sendMessage);
            return;
        }

        if (update.hasPreCheckoutQuery() && update.getPreCheckoutQuery().getId() != null) {
            PreCheckoutQuery checkoutQuery = update.getPreCheckoutQuery();
            AnswerPreCheckoutQuery query = new AnswerPreCheckoutQuery(checkoutQuery.getId(), true);
            sendMessage(query);
            return;
        }

        List<CommandCache> commandCache = commandCacheRepository.findByUserBDOrderById(user);
        if (commandCache.size() > 0) {
            botCommands.botAnswerUtils(commandCache, getReceivedMessage(), getChatId(), user);
            return;
        }

        if (update.hasMessage() && update.getMessage().hasContact()) {
            updateUserProfile(update.getMessage().getContact(), user);
            return;
        }

        isCommand = true;
        botCommands.botAnswerUtils(getReceivedMessage(), getChatId(), user);
    }

    private void updateUserProfile(Contact contact, UserBD userBD) throws Exception {
        userBD.setPhone(contact.getPhoneNumber());
        userRepository.save(userBD);
        botCommands.botAnswerUtils("/afterRegistered", getChatId(), userBD);
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        super.onUpdatesReceived(updates);
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onRegister() {
        super.onRegister();
    }

    public void sendMessage(BotApiMethodMessage message, TransactionType transactionType) {
        try {
            Message reply = execute(message);
            if (message instanceof SendMessage send) {
                saveTransaction(send.getText(), reply.getMessageId(), transactionType);
            }
            if (message instanceof SendInvoice invoice) {
                saveTransaction(invoice.getTitle(), reply.getMessageId(), transactionType);
            }
        } catch (TelegramApiException e) {
            handleAnException(e.getMessage());
        }
    }

    public void sendMessage(BotApiMethodMessage message) {
        sendMessage(message, TransactionType.BOT_MESSAGE);
    }

    public void sendMessage(BotApiMethodBoolean message) {
        try {
            execute(message);
            saveTransaction("Подтверждение оплаты", getMessageId(), TransactionType.PRE_CHECK);
        } catch (TelegramApiException e) {
            handleAnException(e.getMessage());
        }
    }

    private TransactionType getTransactionTypeFromMessage() {
        if (update.hasMessage() && update.getMessage().hasContact()) {
            return TransactionType.CONTACT;
        }
        if (update.hasCallbackQuery()) {
            return TransactionType.CALLBACK_QUERY;
        }
        return TransactionType.MESSAGE;
    }

    private String getReceivedMessage() {
        if (update.hasMessage()) {
            if (update.getMessage().hasSuccessfulPayment()) {
                SuccessfulPayment pay = update.getMessage().getSuccessfulPayment();
                return pay.getTotalAmount() + ";" +
                        pay.getInvoicePayload() + ";" +
                        pay.getTelegramPaymentChargeId() + ";" +
                        orderInfoToString(pay);
            }

            String text = update.getMessage().getText();
            return text == null ? "" : text;
        }
        if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getData();
        }
        return "";
    }

    private String orderInfoToString(SuccessfulPayment pay) {
        return pay.getOrderInfo() == null ? "" :
                pay.getOrderInfo().getName() + "*" + pay.getOrderInfo().getPhoneNumber() + "*" + pay.getOrderInfo().getEmail();
    }

    private String getUserID() {
        return user == null ? "" : String.valueOf(user.getId());
    }

    private long getChatId() {
        if (update.hasMessage()) {
            return update.getMessage().getChatId();
        }
        if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getMessage().getChatId();
        }
        return 0;
    }

    private long getMessageId() {
        if (update.hasMessage()) {
            return update.getMessage().getMessageId();
        }
        if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getMessage().getMessageId();
        }
        return 0;
    }

    private UserBD getUser() {
        User user = null;
        if (update.hasMessage()) {
            user = update.getMessage().getFrom();
        }
        if (update.hasCallbackQuery()) {
            user = update.getCallbackQuery().getFrom();
        }
        if (update.hasMyChatMember()) {
            user = update.getMyChatMember().getFrom();
        }
        return user == null ? null : getUserBD(user);
    }

    private boolean isBot() {
        User user = null;
        if (update.hasMessage()) {
            user = update.getMessage().getFrom();
        }
        if (update.hasCallbackQuery()) {
            user = update.getCallbackQuery().getFrom();
        }
        return user != null && user.getIsBot();
    }

    private UserBD getUserBD(User user) {
        Optional<UserBD> option = userRepository.findById(user.getId());
        return option.orElseGet(() -> createUserBD(user));
    }

    private UserBD createUserBD(User user) {
        UserBD userBD = new UserBD(user);
        userRepository.save(userBD);
        log.info("Create new telegram user {} {}", userBD.getPerson().getFirstName(), userBD.getUserName());
        return userBD;
    }

    private void saveTransaction() {
        saveTransaction(getReceivedMessage(), getMessageId(), getTransactionTypeFromMessage());
    }

    private void saveTransaction(String text) {
        saveTransaction(text, getMessageId(), TransactionType.BOT_MESSAGE);
    }

    private void saveTransaction(String text, long idMessage, TransactionType type) {

        if (text == null || text.isEmpty()) {
            return;
        }

        Transaction transaction = new Transaction();
        transaction.setUserBD(user);
        transaction.setDate(LocalDateTime.now());
        transaction.setText(text);
        transaction.setIdMessage((int) idMessage);
        transaction.setTransactionType(type);
        transaction.setCommand(isCommand);
        transactionRepository.save(transaction);
    }

    private void updateLastActivity() {
        if (user == null) {
            return;
        }
        UserActivity activity = user.getActivity();
        if (activity.getId() != null) {
            activity.setLastActivityDate(LocalDateTime.now());
            activityRepository.save(activity);
        }
    }

    public void handleAnException(String logMessage, String... arguments) {
        handleAnException(collectMessage(logMessage, arguments));
    }

    public void handleAnException(String logMessage) {

        log.error(logMessage);

        if (!isUsed()) {
            return;
        }

        String message = collectMessage("Возникла ошибка при работе бота %s:%s%s",
                botConfig.getBotName(),
                System.lineSeparator(),
                logMessage);

        SendMessage sendMessage = new SendMessage(bugNotifications.getIdTelegramUser(), message);
        sendMessage(sendMessage);
    }

    private boolean isUsed() {
        return bugNotifications.isUse() &&
                bugNotifications.getIdTelegramUser() != null &&
                !bugNotifications.getIdTelegramUser().isEmpty();
    }

    public static String collectMessage(String logMessage, String... arguments) {
        try {
            return String.format(logMessage, arguments);
        } catch (Exception e) {
            log.error(e.getMessage());
            return e.getMessage();
        }
    }
}
