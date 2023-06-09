package com.telegrambot.app.services;

import com.telegrambot.app.config.BotConfig;
import com.telegrambot.app.model.UserActivity;
import com.telegrambot.app.model.UserStatus;
import com.telegrambot.app.model.UserType;
import com.telegrambot.app.repositories.UserActivityRepository;
import com.telegrambot.app.repositories.UserRepository;
import com.telegrambot.app.repositories.UserStatusRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
public class TelegramBotServices extends TelegramLongPollingBot {

    private final BotConfig config;
    private BotCommandsImpl botCommands;
    private final UserActivityRepository activityRepository;
    private final UserRepository userRepository;
    private final UserStatusRepository statusRepository;

    @Autowired
    public TelegramBotServices(BotConfig config, UserRepository userRepository, UserActivityRepository activityRepository, UserStatusRepository statusRepository) {
        this.config = config;
        this.userRepository = userRepository;
        this.activityRepository = activityRepository;
        this.statusRepository = statusRepository;
        this.botCommands = new BotCommandsImpl(this);
    }

    @Override
    public void onUpdateReceived(Update update) {
        long chatId = 0;
        String receivedMessage = "";

        com.telegrambot.app.model.User user = null;

        if (update.hasMessage()) {
            chatId = update.getMessage().getChatId();
            user = getUser(update.getMessage().getFrom());

            if (update.getMessage().hasContact()) {
                updateUserProfile(update.getMessage().getContact(), chatId, user);
            } else {
                receivedMessage = update.getMessage().getText();
            }
        }

        if (update.hasCallbackQuery()) {
            chatId = update.getCallbackQuery().getMessage().getChatId();
            user = getUser(update.getCallbackQuery().getFrom());
            receivedMessage = update.getCallbackQuery().getData();
        }

        if (!(chatId == 0 && receivedMessage.isEmpty())) {
            botCommands.botAnswerUtils(receivedMessage, chatId, user);
        }

        if (user != null) {
            updateLastActivity(user);
        }
    }

    private void updateUserProfile(Contact contact, long chatId, com.telegrambot.app.model.User user) {
        user.setPhone(contact.getPhoneNumber());
        userRepository.save(user);
        updateUserStatus(user, UserType.USER);
        botCommands.botAnswerUtils("/afterRegistered", chatId, user);
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

    public void sentMassage(SendMessage message) {
        try {
            execute(message);
            log.info("Reply sent to {}", message.getChatId());
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private com.telegrambot.app.model.User getUser(User user) {
        com.telegrambot.app.model.User userBD = null;
        Optional<com.telegrambot.app.model.User> option = userRepository.findById(user.getId());
        if (option.isEmpty()) {
            userBD = new com.telegrambot.app.model.User();
            BeanUtils.copyProperties(user, userBD);
            userRepository.save(userBD);
            log.info("Create new telegram user {} {}", userBD.getFirstName(), userBD.getUserName());
            updateUserStatus(userBD);
        }
        return option.orElse(userBD);
    }

    private void updateLastActivity(com.telegrambot.app.model.User user) {
        UserActivity activity = activityRepository.findByUser(user);
        if (activity == null) {
            activity = new UserActivity();
            activity.setUser(user);
        }
        activity.setLastActivityDate(LocalDateTime.now());
        activityRepository.save(activity);
    }

    private void updateUserStatus(com.telegrambot.app.model.User user, UserType type) {
        UserStatus status = new UserStatus();
        status.setUserType(type);
        status.setUser(user);
        status.setLastUpdate(LocalDateTime.now());
        statusRepository.save(status);
        log.info("Update telegram user {} status on {}", user.getFirstName(), status.getUserType());
    }

    private void updateUserStatus(com.telegrambot.app.model.User user) {
        updateUserStatus(user, UserType.UNAUTHORIZED);
    }

}
