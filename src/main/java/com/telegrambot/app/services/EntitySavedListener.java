package com.telegrambot.app.services;

import com.telegrambot.app.DTO.message.Message;
import com.telegrambot.app.DTO.types.OperationType;
import com.telegrambot.app.components.Buttons;
import com.telegrambot.app.model.EntitySavedEvent;
import com.telegrambot.app.model.documents.doc.service.TaskDoc;
import com.telegrambot.app.model.reference.TaskStatus;
import com.telegrambot.app.model.reference.legalentity.Partner;
import com.telegrambot.app.model.transaction.TransactionType;
import com.telegrambot.app.model.types.Document;
import com.telegrambot.app.model.user.UserBD;
import com.telegrambot.app.model.user.UserStatus;
import com.telegrambot.app.repositories.user.UserStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor
public class EntitySavedListener {

    private final UserStatusRepository userStatusRepository;

    private final BalanceService balanceService;
    private final TelegramBotServices botServices;
    private final Buttons button;

    private Object entity;
    private OperationType type;
    private UserBD sourceUser;

    @EventListener
    public void handleEntitySavedEvent(EntitySavedEvent event) {

        entity = event.getSource();
        type = event.getType();
        sourceUser = event.getSourceUser();

        log.info("++ Event {} registered from {} object: {}", type, sourceUser, entity);

        if (entity instanceof Document doc) {
            balanceService.updateBalanceAndFinTransaction(doc);
        }

        if (entity instanceof TaskDoc task) {
            sendNotifyByTaskDoc(task);
        }
    }

    private void sendNotifyByTaskDoc(TaskDoc task) {

        List<UserBD> users = getUserByPartner(task.getPartner());

        if (users == null || users.isEmpty()) {
            return;
        }

        SendMessage sendMessage = getSendMessageByTaskDoc(type, task);
        sendMessageUsers(users, sendMessage);
    }

    private SendMessage getSendMessageByTaskDoc(OperationType type, TaskDoc task) {
        switch (type) {
            case CREATE -> {
                if (!Objects.equals(task.getStatus().getId(), TaskStatus.getClosedStatus().getId())) {
                    String message = Message.getNotifyNewTask(task);
                    return createMessage(message, button.getInlineMarkupByTasks(List.of(task)));
                }
            }
            case UPDATE, EDIT -> {
                if (Objects.equals(task.getStatus().getId(), TaskStatus.getClosedStatus().getId())) {
                    String message = Message.getNotifyClosed(task);
                    return createMessage(message);
                }
            }
            case DEL -> {
                return null;
            }
            default -> {
                return null;
            }
        }
        return null;
    }

    private void sendMessageUsers(List<UserBD> users, SendMessage sendMessage) {

        if (sendMessage == null) {
            return;
        }

        long sourceId = sourceUser == null ? 0 : sourceUser.getId();
        users.stream()
                .filter(user -> user != null && user.getId() != sourceId)
                .forEach(user -> {
                    sendMessage.setChatId(user.getId());
                    botServices.sendMessage(sendMessage, TransactionType.NOTIFY);
                });
    }

    private List<UserBD> getUserByPartner(Partner partner) {
        if (partner == null) {
            return new ArrayList<>();
        }
        List<UserStatus> statuses = userStatusRepository.findByLegal(partner);
        return statuses.stream()
                .map(UserStatus::getUser)
                .toList();
    }

    private SendMessage createMessage(String text) {
        return createMessage(text, null);
    }

    private SendMessage createMessage(String text, ReplyKeyboard keyboard) {
        SendMessage message = new SendMessage();
        message.setText(text);
        message.setReplyMarkup(keyboard);
        return message;
    }
}
