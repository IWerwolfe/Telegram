package com.telegrambot.app.services;

import com.telegrambot.app.DTO.api.DefaultDataResponse;
import com.telegrambot.app.DTO.api.typeОbjects.Entity1C;
import com.telegrambot.app.DTO.types.TaskType;
import com.telegrambot.app.components.Buttons;
import com.telegrambot.app.model.Entity;
import com.telegrambot.app.model.reference.TaskStatus;
import com.telegrambot.app.repositories.CompanyRepository;
import com.telegrambot.app.repositories.EntityRepository;
import com.telegrambot.app.repositories.doc.TaskDocRepository;
import com.telegrambot.app.repositories.reference.ManagerRepository;
import com.telegrambot.app.repositories.reference.TaskStatusRepository;
import com.telegrambot.app.repositories.reference.TaskTypeRepository;
import com.telegrambot.app.services.api.ApiOutServiceImpl;
import com.telegrambot.app.services.converter.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultDataInitializer implements CommandLineRunner {

    private final CompanyConverter companyConverter;
    private final CompanyRepository companyRepository;
    private final ManagerRepository managerRepository;
    private final TaskDocRepository taskDocRepository;

    private final ApiOutServiceImpl api1C;
    private final TaskTypeRepository typeRepository;
    private final TaskTypeConverter typeConverter;
    private final TaskStatusConverter statusConverter;
    private final TaskStatusRepository statusRepository;
    private final ManagerConverter managerConverter;

    private final String DEFAULT_NAME_CLOSED = "Завершена";
    private final String DEFAULT_NAME_INIT = "Ожидает обработки";
    private final String DEFAULT_NAME_TYPE = "Обращение через чат бот";

    @Override
    public void run(String... args) throws Exception {

        Buttons.init();
        DefaultDataResponse data = null;
        try {
            data = api1C.getDefaultData();
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        if (data == null || !data.isResult()) {
            TaskType.setDefaultType(getDefaultType());
            TaskStatus.setDefaultInitialStatus(getDefaultByName(DEFAULT_NAME_INIT));
            TaskStatus.setDefaultClosedStatus(getDefaultByName(DEFAULT_NAME_CLOSED));
            log.info("Creating new default data success");
            return;
        }

        updateEntity(data.getTaskTypes(), typeRepository, typeConverter);
        updateEntity(data.getTaskStatuses(), statusRepository, statusConverter);
        updateEntity(data.getManagers(), managerRepository, managerConverter);
        updateEntity(data.getCompanies(), companyRepository, companyConverter);
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
        return optional.orElseGet(() -> typeRepository.save(new TaskType(null, DEFAULT_NAME_TYPE)));
    }

    private <T extends Entity1C, E extends Entity, R extends EntityRepository<E>, C extends Converter1C> void updateEntity(List<T> list,
                                                                                                                           R repository,
                                                                                                                           C converter) {
        if (list == null || list.isEmpty()) return;
        List<E> entityList = new ArrayList<>();
        for (T response : list) {
            entityList.add(converter.convertToEntity(response));
        }
        repository.saveAll(entityList);
    }
}
