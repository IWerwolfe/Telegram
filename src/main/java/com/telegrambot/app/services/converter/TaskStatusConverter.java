package com.telegrambot.app.services.converter;

import com.telegrambot.app.DTO.api_1C.taskResponse.TaskStatusResponse;
import com.telegrambot.app.DTO.api_1C.typesОbjects.Entity1C;
import com.telegrambot.app.model.Entity;
import com.telegrambot.app.model.documents.docdata.SyncData;
import com.telegrambot.app.model.reference.TaskStatus;
import com.telegrambot.app.repositories.TaskStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TaskStatusConverter extends Converter1C {
    private final TaskStatusRepository statusRepository;

    @Override
    public <T extends Entity, R extends Entity1C> R convertToResponse(T entity) {
        return null;
    }

    @Override
    public <T extends Entity, R extends Entity1C> T updateEntity(R dto, T entity) {
        if (dto instanceof TaskStatusResponse response && entity instanceof TaskStatus entityBD) {
            entityBD.setName(response.getName());
            entityBD.setSyncData(new SyncData(response.getGuid(), response.getCode()));
            return entity;
        }
        return null;
    }

    @Override
    public <T extends Entity, R extends Entity1C> T getOrCreateEntity(R dto) {
        return (T) Converter1C.getOrCreateEntity(dto, statusRepository, TaskStatus.class);
    }

    @Override
    public <T extends Entity> T getOrCreateEntity(String guid, boolean isSaved) {
        return (T) Converter1C.getOrCreateEntity(guid, statusRepository, TaskStatus.class, isSaved);
    }
}
