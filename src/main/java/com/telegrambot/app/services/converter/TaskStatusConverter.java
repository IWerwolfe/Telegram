package com.telegrambot.app.services.converter;

import com.telegrambot.app.DTO.api.taskStatus.TaskStatusResponse;
import com.telegrambot.app.DTO.api.typeОbjects.Entity1C;
import com.telegrambot.app.model.Entity;
import com.telegrambot.app.model.reference.Reference;
import com.telegrambot.app.model.reference.TaskStatus;
import com.telegrambot.app.repositories.reference.TaskStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TaskStatusConverter extends Converter1C {

    private final Class<TaskStatus> classType = TaskStatus.class;
    private final TaskStatusRepository repository;

    @Override
    public <T extends Entity, R extends Entity1C> R convertToResponse(T entity) {
        if (entity instanceof Reference ref) {
            return convertReferenceToResponse(ref);
        }
        return null;
    }

    @Override
    public <T extends Entity, R extends Entity1C> T updateEntity(R dto, T entity) {
        if (dto instanceof TaskStatusResponse response && entity instanceof TaskStatus entityBD) {
            entityBD.setName(response.getName());
            return entity;
        }
        return null;
    }

    @Override
    public <T extends Entity, R extends Entity1C> T getOrCreateEntity(R dto) {
        return (T) Converter1C.getOrCreateEntity(dto, repository, classType);
    }

    @Override
    public <T extends Entity> T getOrCreateEntity(String guid, boolean isSaved) {
        return (T) Converter1C.getOrCreateEntity(guid, repository, classType, isSaved);
    }
}
