package com.telegrambot.app.services;

import com.telegrambot.app.DTO.api_1C.DefaultDataResponse;
import com.telegrambot.app.DTO.api_1C.TaskStatusResponse;
import com.telegrambot.app.DTO.api_1C.TaskTypeResponse;
import com.telegrambot.app.model.task.TaskStatus;
import com.telegrambot.app.model.task.TaskType;
import com.telegrambot.app.repositories.TaskStatusRepository;
import com.telegrambot.app.repositories.TaskTypeRepository;
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

    private final API1CServicesImpl api1C;
    private final TaskTypeRepository typeRepository;
    private final TaskTypeConverter typeConverter;
    private final TaskStatusConverter statusConverter;
    private final TaskStatusRepository statusRepository;

    @Override
    public void run(String... args) throws Exception {

        DefaultDataResponse data = api1C.getDefaultData();
        if (data == null || !data.isResult()) {
            TaskType.setDefaultType(getDefaultType());
            TaskStatus.setDefaultInitialStatus(getDefaultInitialStatus());
            TaskStatus.setDefaultClosedStatus(getDefaultClosedStatus());
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

        Optional<TaskStatus> statusOptional = statusRepository.findByGuid(data.getGuidDefaultInitialStatus());
        TaskStatus taskStatus = statusOptional.orElseGet(this::getDefaultInitialStatus);
        TaskStatus.setDefaultInitialStatus(taskStatus);

        statusOptional = statusRepository.findByGuid(data.getGuidDefaultClosedStatus());
        taskStatus = statusOptional.orElseGet(this::getDefaultClosedStatus);
        TaskStatus.setDefaultClosedStatus(taskStatus);
    }

    private TaskStatus getDefaultClosedStatus() {
        return statusRepository.save(new TaskStatus("Закрыта"));
    }

    private TaskStatus getDefaultInitialStatus() {
        return statusRepository.save(new TaskStatus("Ожидает обработки"));
    }

    private TaskType getDefaultType() {
        return typeRepository.save(new TaskType("Обращение"));
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
