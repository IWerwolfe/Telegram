package com.supportbot.bot;

import com.supportbot.DTO.message.MessageText;
import com.supportbot.config.BotConfig;
import com.supportbot.config.BugNotifications;
import com.supportbot.model.transaction.Transaction;
import com.supportbot.model.transaction.TransactionType;
import com.supportbot.model.user.UserActivity;
import com.supportbot.model.user.UserBD;
import com.supportbot.repositories.transaction.TransactionRepository;
import com.supportbot.repositories.user.UserActivityRepository;
import com.supportbot.repositories.user.UserRepository;
import com.supportbot.services.BotCommandsImpl;
import com.supportbot.services.SenderService;
import com.supportbot.utils.TextUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerPreCheckoutQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.payments.PreCheckoutQuery;
import org.telegram.telegrambots.meta.api.objects.payments.SuccessfulPayment;

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
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final SenderService senderService;
    private final TextUtils textUtils;
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

            String text = textUtils.collectMessage("У пользователя с id: %s изменился статус на %s", getUserID(), newStatus);
            saveTransaction(text);
            log.warn(text);
            return;
        }

        boolean isGroupMessage = update.hasMessage() &&
                update.getMessage().getChat() != null &&
                !update.getMessage().getChat().getType().equals("private");

        if (this.user == null) {
            String text = MessageText.getSkippingMessageFromUnknownUser(getReceivedMessage());
            saveTransaction(text);
            log.warn(text);
            return;
        }

        if (isGroupMessage) {
            saveTransaction(MessageText.getSkippingMessageFromGroup());
            return;
        }

        try {
            saveTransaction();
            handlerMessage(update);
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
            senderService.sendBotMessage(this, sendMessage, user);
            return;
        }

        if (update.hasPreCheckoutQuery() && update.getPreCheckoutQuery().getId() != null) {
            PreCheckoutQuery checkoutQuery = update.getPreCheckoutQuery();
            AnswerPreCheckoutQuery query = new AnswerPreCheckoutQuery(checkoutQuery.getId(), true);
            senderService.sendPreCheckMessage(this, query, user);
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
                return textUtils.collectMessage("%s;%s;%s;%s",
                        String.valueOf(pay.getTotalAmount()),
                        pay.getInvoicePayload(),
                        pay.getTelegramPaymentChargeId(),
                        orderInfoToString(pay));
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
                textUtils.collectMessage("%s*%s*%s",
                        pay.getOrderInfo().getName(),
                        pay.getOrderInfo().getPhoneNumber(),
                        pay.getOrderInfo().getEmail());
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

    private User getUserBot() {

        if (update.hasMessage()) {
            return update.getMessage().getFrom();
        }
        if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getFrom();
        }
        if (update.hasMyChatMember()) {
            return update.getMyChatMember().getFrom();
        }
        return null;
    }

    private UserBD getUser() {
        User user = getUserBot();
        return user == null ? null : getUserBD(user);
    }

    private boolean isBot() {
        User user = getUserBot();
        return user != null && user.getIsBot();
    }

    private UserBD getUserBD(User user) {
        Optional<UserBD> option = userRepository.findById(user.getId());
        return option.orElseGet(() -> createUserBD(user));
    }

    private UserBD createUserBD(User user) {
        UserBD userBD = new UserBD(user);
        log.info("Create new telegram user {} {}", userBD.getPerson().getFirstName(), userBD.getUserName());
        return userRepository.save(userBD);
    }

    private void saveTransaction() {
        saveTransaction(getReceivedMessage(), getMessageId(), getTransactionTypeFromMessage());
    }

    private void saveTransaction(String text) {
        saveTransaction(text, getMessageId(), TransactionType.BOT_MESSAGE);
    }

    private void saveTransaction(String text, long idMessage, TransactionType type) {
        saveTransaction(text, idMessage, type, user);
    }

    public void saveTransaction(String text, long idMessage, TransactionType type, UserBD user) {

        if (text == null || text.isEmpty()) {
            return;
        }

        Transaction transaction = new Transaction();
        transaction.setUserBD(user);
        transaction.setDate(LocalDateTime.now());
        transaction.setText(text);
        transaction.setIdMessage((int) idMessage);
        transaction.setTransactionType(type);
        transaction.setCommand(false);
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
        handleAnException(textUtils.collectMessage(logMessage, arguments));
    }

    public void handleAnException(String logMessage) {

        log.error(logMessage);

        if (!isUsed()) {
            return;
        }

        String message = textUtils.collectMessage("Возникла ошибка при работе бота %s:%s%s",
                botConfig.getBotName(),
                System.lineSeparator(),
                logMessage);

        SendMessage sendMessage = new SendMessage(bugNotifications.getIdTelegramUser(), message);
        senderService.sendErrorMessage(this, sendMessage, user);
    }

    private boolean isUsed() {
        return bugNotifications.isUse() &&
                bugNotifications.getIdTelegramUser() != null &&
                !bugNotifications.getIdTelegramUser().isEmpty();
    }
}
