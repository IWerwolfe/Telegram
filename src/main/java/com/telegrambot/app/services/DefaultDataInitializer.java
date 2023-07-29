package com.telegrambot.app.services;

import com.telegrambot.app.DTO.api_1C.DefaultDataResponse;
import com.telegrambot.app.DTO.api_1C.taskResponse.TaskStatusResponse;
import com.telegrambot.app.DTO.api_1C.taskResponse.TaskTypeResponse;
import com.telegrambot.app.DTO.types.TaskType;
import com.telegrambot.app.components.Buttons;
import com.telegrambot.app.model.reference.TaskStatus;
import com.telegrambot.app.repositories.TaskRepository;
import com.telegrambot.app.repositories.TaskStatusRepository;
import com.telegrambot.app.repositories.TaskTypeRepository;
import com.telegrambot.app.services.api.API1CServicesImpl;
import com.telegrambot.app.services.converter.TaskStatusConverter;
import com.telegrambot.app.services.converter.TaskTypeConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultDataInitializer implements CommandLineRunner {
    private final TaskRepository taskRepository;

    private final API1CServicesImpl api1C;
    private final TaskTypeRepository typeRepository;
    private final TaskTypeConverter typeConverter;
    private final TaskStatusConverter statusConverter;
    private final TaskStatusRepository statusRepository;

    private final String DEFAULT_NAME_CLOSED = "Завершена";
    private final String DEFAULT_NAME_INIT = "Ожидает обработки";
    private final String DEFAULT_NAME_TYPE = "ОбращениеЧерезЧатБот";

    @Override
    public void run(String... args) throws Exception {

        Buttons.init();
        DefaultDataResponse data = api1C.getDefaultData();
        if (data == null || !data.isResult()) {
            TaskType.setDefaultType(getDefaultType());
            TaskStatus.setDefaultInitialStatus(getDefaultByName(DEFAULT_NAME_INIT));
            TaskStatus.setDefaultClosedStatus(getDefaultByName(DEFAULT_NAME_CLOSED));
            log.info("Creating new default data success");
            return;
        }

        updateTaskTypes(data.getTaskTypes());
        updateTaskStatuses(data.getTaskStatuses());
        updateDefaultInfo(data);
        log.info("Loading default data success");
    }

    private void updateDefaultInfo(DefaultDataResponse data) {

        Optional<TaskType> typeOptional = typeRepository.findByNameIgnoreCase(data.getNameDefaultType());
        TaskType taskType = typeOptional.orElseGet(this::getDefaultType);
        TaskType.setDefaultType(taskType);

        Optional<TaskStatus> statusOptional = statusRepository.findBySyncDataNotNullAndSyncData_Guid(data.getGuidDefaultInitialStatus());
        TaskStatus taskStatus = statusOptional.orElseGet(() -> getDefaultByName(DEFAULT_NAME_INIT));
        TaskStatus.setDefaultInitialStatus(taskStatus);

        statusOptional = statusRepository.findBySyncDataNotNullAndSyncData_Guid(data.getGuidDefaultClosedStatus());
        taskStatus = statusOptional.orElseGet(() -> getDefaultByName(DEFAULT_NAME_CLOSED));
        TaskStatus.setDefaultClosedStatus(taskStatus);
    }

    private TaskStatus getDefaultByName(String name) {
        Optional<TaskStatus> optional = statusRepository.findByNameIgnoreCase(name);
        return optional.orElseGet(() -> statusRepository.save(new TaskStatus(null, name)));
    }

    private TaskType getDefaultType() {
        Optional<TaskType> optional = typeRepository.findByNameIgnoreCase(DEFAULT_NAME_TYPE);
        return optional.orElseGet(() -> typeRepository.save(new TaskType(DEFAULT_NAME_TYPE)));
    }


    private void updateTaskTypes(List<TaskTypeResponse> list) {
        if (list == null || list.isEmpty()) return;
        for (TaskTypeResponse response : list) {
            TaskType type = typeConverter.convertToEntity(response);
            typeRepository.save(type);
        }
    }

    private void updateTaskStatuses(List<TaskStatusResponse> list) {
        if (list == null || list.isEmpty()) return;
        for (TaskStatusResponse response : list) {
            TaskStatus status = statusConverter.convertToEntity(response);
            statusRepository.save(status);
        }
    }
}
