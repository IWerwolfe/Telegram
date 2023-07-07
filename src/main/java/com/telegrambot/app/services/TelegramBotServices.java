package com.telegrambot.app.services;

import com.telegrambot.app.config.BotConfig;
import com.telegrambot.app.model.PersonFields;
import com.telegrambot.app.model.command.CommandCache;
import com.telegrambot.app.model.transaction.Transaction;
import com.telegrambot.app.model.transaction.TransactionType;
import com.telegrambot.app.model.user.UserActivity;
import com.telegrambot.app.model.user.UserBD;
import com.telegrambot.app.model.user.UserStatus;
import com.telegrambot.app.model.user.UserType;
import com.telegrambot.app.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TelegramBotServices extends TelegramLongPollingBot {

    private final BotConfig config;
    private final BotCommandsImpl botCommands;
    private final UserActivityRepository activityRepository;
    private final CommandCacheRepository commandCacheRepository;
    private final UserRepository userRepository;
    private final UserStatusRepository statusRepository;
    private final TransactionRepository transactionRepository;
    private Update update;

    @Override
    public void onUpdateReceived(Update update) {

        botCommands.setParent(this);

        boolean isCommand = true;
        this.update = update;

        List<CommandCache> commandCache = commandCacheRepository.findByUserBDOrderById(getUser());
        if (commandCache.size() > 0) {
            botCommands.botAnswerUtils(commandCache, getReceivedMessage(), getChatId(), getUser());
            isCommand = false;
            log.info("The text command processing mode is set");
        }

        if (update.hasMessage() && update.getMessage().hasContact()) {
            updateUserProfile(update.getMessage().getContact(), getChatId(), getUser());
            isCommand = false;
        }

        saveTransaction(isCommand);

        if (isCommand) {
            botCommands.botAnswerUtils(getReceivedMessage(), getChatId(), getUser());
        }

        updateLastActivity(getUser());


    }

    private void updateUserProfile(Contact contact, long chatId, UserBD userBD) {
        userBD.setPhone(contact.getPhoneNumber());
        userRepository.save(userBD);
//        updateUserStatus(userBD, UserType.USER);
        botCommands.botAnswerUtils("/afterRegistered", chatId, userBD);
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


    public void sendMassage(SendMessage message) {
        try {
            execute(message);
            saveTransaction(message);
            log.info("Reply sent to {}", message.getChatId());
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
            return update.getMessage().getText();
        }
        if (update.hasCallbackQuery()) {
            return update.getCallbackQuery().getData();
        }
        return "";
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

    private UserBD getUser() {
        User user = null;
        if (update.hasMessage()) {
            user = update.getMessage().getFrom();
        }
        if (update.hasCallbackQuery()) {
            user = update.getCallbackQuery().getFrom();
        }
        return user == null ? null : getUser(user);
    }

    private UserBD getUser(User user) {
        UserBD userBD = null;
        Optional<UserBD> option = userRepository.findById(user.getId());
        if (option.isEmpty()) {
            userBD = getUserBD(user);
            log.info("Create new telegram user {} {}", userBD.getPerson().getFirstName(), userBD.getUserName());
            updateUserStatus(userBD);
        }
        return option.orElse(userBD);
    }

    private UserBD getUserBD(User user) {
        UserBD userBD = new UserBD();
        BeanUtils.copyProperties(user, userBD);
        PersonFields person = new PersonFields(user.getFirstName(), user.getLastName());
        userBD.setPerson(person);
        return userRepository.save(userBD);
    }

    private void saveTransaction(boolean isCommand) {
        Transaction transaction = new Transaction();
        transaction.setUserBD(getUser());
        transaction.setDate(LocalDateTime.now());
        transaction.setText(getReceivedMessage());
        transaction.setIdMessage((int) getChatId());
        transaction.setTransactionType(getTransactionTypeFromMessage());
        transaction.setCommand(isCommand);
        transactionRepository.save(transaction);
    }

    private void saveTransaction(SendMessage sendMessage) {
        Transaction transaction = new Transaction();
        transaction.setUserBD(getUser());
        transaction.setDate(LocalDateTime.now());
        transaction.setText(sendMessage.getText());
        transaction.setIdMessage(Integer.valueOf(sendMessage.getChatId()));
        transaction.setTransactionType(TransactionType.BOT_MESSAGE);
        transaction.setCommand(false);
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

    private void updateUserStatus(UserBD userBD) {
        updateUserStatus(userBD, UserType.UNAUTHORIZED);
    }

}
