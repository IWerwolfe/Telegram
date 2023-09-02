package com.telegrambot.app.services;

import com.telegrambot.app.config.BotConfig;
import com.telegrambot.app.model.balance.UserBalance;
import com.telegrambot.app.model.command.CommandCache;
import com.telegrambot.app.model.documents.docdata.PersonData;
import com.telegrambot.app.model.transaction.Transaction;
import com.telegrambot.app.model.transaction.TransactionType;
import com.telegrambot.app.model.user.UserActivity;
import com.telegrambot.app.model.user.UserBD;
import com.telegrambot.app.model.user.UserStatus;
import com.telegrambot.app.model.user.UserType;
import com.telegrambot.app.repositories.balance.UserBalanceRepository;
import com.telegrambot.app.repositories.command.CommandCacheRepository;
import com.telegrambot.app.repositories.transaction.TransactionRepository;
import com.telegrambot.app.repositories.user.UserActivityRepository;
import com.telegrambot.app.repositories.user.UserRepository;
import com.telegrambot.app.repositories.user.UserStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerPreCheckoutQuery;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodBoolean;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.methods.invoices.SendInvoice;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
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
public class TelegramBotServices extends TelegramLongPollingBot {
    private final UserBalanceRepository userBalanceRepository;

    private final BotConfig config;
    private final BotCommandsImpl botCommands;
    private final UserActivityRepository activityRepository;
    private final CommandCacheRepository commandCacheRepository;
    private final UserRepository userRepository;
    private final UserStatusRepository statusRepository;
    private final TransactionRepository transactionRepository;
    private Update update;
    private UserBD user;

    public void onUpdateReceived(Update update) {

        botCommands.setParent(this);
        boolean isCommand = true;
        this.update = update;
        user = getUser();

        if (update.hasPreCheckoutQuery() && update.getPreCheckoutQuery().getId() != null) {
            PreCheckoutQuery checkoutQuery = update.getPreCheckoutQuery();
            AnswerPreCheckoutQuery query = new AnswerPreCheckoutQuery(checkoutQuery.getId(), true);
            sendMassage(query);
            return;
        }

        List<CommandCache> commandCache = commandCacheRepository.findByUserBDOrderById(user);
        if (commandCache.size() > 0) {
            botCommands.botAnswerUtils(commandCache, getReceivedMessage(), getChatId(), user);
            isCommand = false;
            log.info("The text command processing mode is set");
        }

        if (update.hasMessage() && update.getMessage().hasContact()) {
            updateUserProfile(update.getMessage().getContact(), user);
            isCommand = false;
        }

        saveTransaction(isCommand);

        if (isCommand) {
            botCommands.botAnswerUtils(getReceivedMessage(), getChatId(), user);
        }
        updateLastActivity(user);
    }

    private void updateUserProfile(Contact contact, UserBD userBD) {
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
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onRegister() {
        super.onRegister();
    }


    public void sendMassage(BotApiMethodMessage message) {
        try {
            execute(message);
            if (message instanceof SendMessage send) {
                saveTransaction(send);
            }
            if (message instanceof SendInvoice invoice) {
                saveTransaction(invoice.getTitle());
            }
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    public void sendMassage(BotApiMethodBoolean message) {
        try {
            execute(message);
            saveTransaction("Подтверждение оплаты");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
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

            return update.getMessage().getText();
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
        return user == null ? null : getUserBD(user);
    }

    private UserBD getUserBD(User user) {
        Optional<UserBD> option = userRepository.findById(user.getId());
        return option.orElseGet(() -> createUserBD(user));
    }

    private UserBD createUserBD(User user) {
        UserBD userBD = new UserBD();
        BeanUtils.copyProperties(user, userBD);
        PersonData person = new PersonData(user.getFirstName(), user.getLastName());
        userBD.setPerson(person);

        userRepository.save(userBD);
        updateUserStatus(userBD, UserType.UNAUTHORIZED);
        userBalanceRepository.save(new UserBalance(userBD));

        log.info("Create new telegram user {} {}", userBD.getPerson().getFirstName(), userBD.getUserName());
        return userBD;
    }

    private void saveTransaction(boolean isCommand) {
        saveTransaction(getReceivedMessage(), isCommand);
    }

    private void saveTransaction(SendMessage sendMessage) {
        saveTransaction(sendMessage.getText());
    }

    private void saveTransaction(String text) {
        saveTransaction(text, false);
    }

    private void saveTransaction(String text, boolean isCommand) {
        Transaction transaction = new Transaction();
        transaction.setUserBD(user);
        transaction.setDate(LocalDateTime.now());
        transaction.setText(text);
        transaction.setIdMessage((int) getMessageId());
        transaction.setTransactionType(TransactionType.BOT_MESSAGE);
        transaction.setCommand(isCommand);
        transactionRepository.save(transaction);
    }

    private void updateLastActivity(UserBD userBD) {
        UserActivity activity = activityRepository.findByUserBD(userBD);
        if (activity == null) {
            activity = new UserActivity();
            activity.setUserBD(userBD);
        }
        activity.setLastActivityDate(LocalDateTime.now());
        activityRepository.save(activity);
    }

    private void updateUserStatus(UserBD userBD, UserType type) {
        UserStatus status = new UserStatus();
        status.setUserType(type);
        status.setUserBD(userBD);
        status.setLastUpdate(LocalDateTime.now());
        statusRepository.save(status);
        log.info("Update telegram userBD {} status on {}", userBD.getPerson().getFirstName(), status.getUserType());
    }
}
