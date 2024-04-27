package com.supportbot.listeners;

import com.supportbot.DTO.message.MessageText;
import com.supportbot.DTO.types.EventSource;
import com.supportbot.DTO.types.OperationType;
import com.supportbot.bot.SupportBot;
import com.supportbot.components.Buttons;
import com.supportbot.config.SystemNotifications;
import com.supportbot.config.UserNotifications;
import com.supportbot.model.EntitySavedEvent;
import com.supportbot.model.documents.doc.service.TaskDoc;
import com.supportbot.model.reference.TaskStatus;
import com.supportbot.model.reference.legalentity.Partner;
import com.supportbot.model.types.Document;
import com.supportbot.model.user.UserBD;
import com.supportbot.model.user.UserStatus;
import com.supportbot.repositories.user.UserStatusRepository;
import com.supportbot.services.BalanceService;
import com.supportbot.services.SenderService;
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
    private final Buttons button;
    private final SystemNotifications systemNotifications;
    private final UserNotifications userNotifications;
    private final SenderService senderService;
    private final SupportBot bot;

    private OperationType type;
    private UserBD sourceUser;
    private EventSource eventSource;

    @EventListener
    public void handleEntitySavedEvent(EntitySavedEvent event) {

        Object entity = event.getSource();
        type = event.getType();
        sourceUser = event.getSourceUser();
        eventSource = event.getEventSource();

        log.info("++ Event {} registered from {} object: {}", type, sourceUser, entity);

        if (entity instanceof Document doc) {
            balanceService.updateBalanceAndFinTransaction(doc);
        }

        if (entity instanceof TaskDoc task) {
            sendNotifyByTaskDoc(task);
        }
    }

    private void sendNotifyByTaskDoc(TaskDoc task) {

        if (userNotifications.isUse()) {
            sendTaskDocNotifyToUsers(task);
        }
        if (systemNotifications.isUse()) {
            sendTaskDocNotifyToSystem(task);
        }
    }

    private void sendTaskDocNotifyToUsers(TaskDoc task) {
        List<UserBD> users = getUserByPartner(task.getPartner());

        if (users == null || users.isEmpty()) {
            return;
        }

        SendMessage sendMessage = getSendMessageByTaskDocForUsers(task);
        sendMessageUsers(users, sendMessage);
    }

    private void sendTaskDocNotifyToSystem(TaskDoc task) {
        if (systemNotifications.getIdToWorkGroup() == null || systemNotifications.getIdToWorkGroup().isEmpty()) {
            return;
        }
        SendMessage sendMessage = getSendMessageByTaskDocForSystem(task);
        if (sendMessage != null) {
            sendMessageUser(Long.valueOf(systemNotifications.getIdToWorkGroup()), sendMessage);
        }
    }

    private SendMessage getSendMessageByTaskDocForSystem(TaskDoc task) {
        boolean isClosed = Objects.equals(task.getStatus().getId(), TaskStatus.getClosedStatus().getId());
        switch (type) {
            case CREATE -> {
                if (systemNotifications.isSendCreateNewTask() && eventSource == EventSource.USER) {
                    String message = MessageText.getSystemNotifyNewTask(task, sourceUser);
                    return createMessage(message);
                }
            }
            case UPDATE, EDIT -> {
                if (isClosed && systemNotifications.isUserClosedTask() && eventSource == EventSource.USER) {
                    String message = MessageText.getSystemNotifyOfUserClosure(task, sourceUser);
                    return createMessage(message);
                }
                if (isClosed && systemNotifications.isClosedTask() && eventSource != EventSource.USER) {
                    String message = MessageText.getSystemNotifyClosed(task);
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

    private SendMessage getSendMessageByTaskDocForUsers(TaskDoc task) {
        boolean isClosed = Objects.equals(task.getStatus().getId(), TaskStatus.getClosedStatus().getId());
        switch (type) {
            case CREATE -> {
                if (!isClosed && userNotifications.isSendCreateNewTask()) {
                    String message = MessageText.getNotifyNewTask(task, sourceUser);
                    return createMessage(message, button.getInlineMarkupByTasks(List.of(task)));
                }
            }
            case UPDATE, EDIT -> {
                if (isClosed && userNotifications.isClosedTask()) {
                    String message = MessageText.getNotifyClosed(task, sourceUser);
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
                .filter(user -> user != null && user.getId() != sourceId && !user.getNotValid())
                .toList()
                .forEach(user -> sendMessageUser(user.getId(), sendMessage));
    }

    private void sendMessageUser(Long id, SendMessage sendMessage) {
        sendMessage.setChatId(id);
        senderService.sendNotifyMessage(bot, sendMessage, null);
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
