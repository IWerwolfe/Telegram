package com.supportbot.bot.command;

import com.supportbot.DTO.TaskListToSend;
import com.supportbot.DTO.api.doc.taskDoc.TaskDocDataResponse;
import com.supportbot.DTO.api.typeObjects.DataResponse;
import com.supportbot.DTO.message.MessageText;
import com.supportbot.DTO.types.SortingTaskType;
import com.supportbot.bot.SupportBot;
import com.supportbot.client.ApiClient;
import com.supportbot.components.Buttons;
import com.supportbot.model.documents.doc.service.TaskDoc;
import com.supportbot.model.reference.TaskStatus;
import com.supportbot.model.types.Entity;
import com.supportbot.model.types.Reference;
import com.supportbot.model.user.UserBD;
import com.supportbot.model.user.UserStatus;
import com.supportbot.repositories.doc.TaskDocRepository;
import com.supportbot.services.SenderService;
import com.supportbot.services.TaskDocService;
import com.supportbot.services.UserBotService;
import com.supportbot.services.converter.TaskDocConverter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.IBotCommand;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class GetTaskCommand implements IBotCommand {

    private final SenderService senderService;
    private final TaskDocRepository taskDocRepository;
    private final Buttons button;
    private final UserBotService userBotService;
    private final TaskDocConverter taskDocConverter;
    private final ApiClient api1C;
    private final TaskDocService taskDocService;

    private SupportBot supportBot;
    private UserBD user;
    private long chatId;

    @Override
    public String getCommandIdentifier() {
        return "get_task";
    }

    @Override
    public String getDescription() {
        return "Начало работы с ботом";
    }

    @Override
    public void processMessage(AbsSender absSender, Message message, String[] arguments) {

        user = userBotService.getUser(message.getFrom());
        chatId = message.getChatId();
        supportBot = (SupportBot) absSender;

        switch (arguments.length) {
            case 0 -> taskListHandler();
            case 1 -> taskListHandler(arguments[0]);
            default -> sendMessage("Не корректно указан номер обращения");
        }
    }

    private void taskListHandler(String taskId) {

        if (taskId.matches("000[0-9]{6}")) {
            TaskDocDataResponse response = api1C.getTaskByCode(taskId);
            if (isCompleted(response)) {
                TaskDoc taskDoc = taskDocConverter.convertToEntity(response.getEntity());
                sendMessage(taskDoc.toString(true), button.getInlineMarkupEditTask(taskDoc));
                return;
            }
        }

        if (taskId.matches("[0-9]")) {
            Optional<TaskDoc> optional = taskDocRepository.findById(Long.valueOf(taskId));
            if (optional.isPresent()) {
                TaskDoc taskDoc = optional.get();
                String text = taskDoc.toString(true);
                InlineKeyboardMarkup keyboard = button.getInlineMarkupEditTask(taskDoc);
                sendMessage(text, keyboard);
                return;
            }
        }
        sendMessage("Не корректно указан номер обращения");
    }

    private void taskListHandler() {
        List<UserStatus> statuses = user.getStatuses();
        switch (statuses.size()) {
            case 0 -> sendMessage(MessageText.getSearchErrors());
            case 1 -> subSendTaskList(getSortedTask(getTaskListToSend(statuses.get(0))));
            default -> statuses.forEach(s -> {
                TaskListToSend taskListToSend = getTaskListToSend(s);
                sendMessage(MessageText.getSearch(getNameEntity(s.getLegal()), taskListToSend.getTaskDocs().size()));
                subSendTaskList(getSortedTask(taskListToSend));
            });
        }
    }

    private Map<String, List<TaskDoc>> getSortedTask(TaskListToSend taskListToSend) {
        return taskListToSend
                .getTaskDocs()
                .stream()
                .collect(Collectors.groupingBy(task -> getStringSorting(taskListToSend, task)));
    }

    private void subSendTaskList(Map<String, List<TaskDoc>> sortedTask) {

        if (sortedTask.isEmpty()) {
            sendMessage(MessageText.getSearchErrors());
            return;
        }
        sortedTask.keySet().forEach(sortName -> {
            List<TaskDoc> taskDocList = sortedTask.get(sortName);
            String message = MessageText.getSearchGrouping(sortName, taskDocList.size());
            ReplyKeyboard keyboard = button.getInlineMarkupByTasks(taskDocList);
            sendMessage(message, keyboard);
        });
    }

    private TaskListToSend getTaskListToSend(@NonNull UserStatus status) {

        List<TaskDoc> taskDocs = new ArrayList<>();
        SortingTaskType sortingType = SortingTaskType.DEPARTMENT;

        switch (status.getUserType()) {
            case UNAUTHORIZED, USER -> {
                if (TaskStatus.getClosedStatus() != null) {
                    taskDocs = taskDocService.getTaskListByApiByUser(user);
                }
            }
            case ADMINISTRATOR -> taskDocs = status.getDepartment() == null ?
                    taskDocService.getTaskListByApiByCompany(status.getLegal()) :
                    taskDocService.getTaskListByApiByDepartment(status.getDepartment());
            case DIRECTOR -> taskDocs = taskDocService.getTaskListByApiByCompany(status.getLegal());
            default -> {
                if (user.getIsMaster()) {
                    //TODO Написать связь Manager и UserResponse
//                    taskDocs = getTaskListByApiByManager();
//                    sortingType = SortingTaskType.PARTNER;
                }
            }
        }
        return new TaskListToSend(sortingType, taskDocs);
    }

    private String getStringSorting(TaskListToSend taskListToSend, TaskDoc taskDoc) {
        return switch (taskListToSend.getSorting()) {
            case STATUS -> getNameEntity(taskDoc.getStatus());
            case PARTNER -> getNamePartner(taskDoc);
            default -> getNameDepartment(taskDoc);
        };
    }

    private String getNameEntity(Entity entity) {
        if (entity == null) {
            return "-";
        }
        if (entity instanceof Reference ref) {
            return ref.toString();
        }
        return entity.getClass().getSimpleName();
    }

    private String getNamePartner(TaskDoc taskDoc) {
        return taskDoc.getPartnerData() == null ? "-" : getNameEntity(taskDoc.getPartnerData().getPartner());
    }

    private String getNameDepartment(TaskDoc taskDoc) {
        return taskDoc.getPartnerData() == null ? "-" : getNameEntity(taskDoc.getPartnerData().getDepartment());
    }

    private void sendMessage(@NonNull String message, ReplyKeyboard keyboard) {
        senderService.sendBotMessage(supportBot, message, keyboard, chatId, user);
    }

    private void sendMessage(@NonNull String message) {
        senderService.sendBotMessage(supportBot, message, null, chatId, user);
    }

    private static <R extends DataResponse> boolean isCompleted(R response) {
        return response != null && response.isResult();
    }
}
