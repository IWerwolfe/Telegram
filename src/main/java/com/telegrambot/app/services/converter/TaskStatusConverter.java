package com.telegrambot.app.services.converter;

import com.telegrambot.app.DTO.api.reference.taskStatus.TaskStatusResponse;
import com.telegrambot.app.DTO.api.typeОbjects.EntityResponse;
import com.telegrambot.app.model.reference.TaskStatus;
import com.telegrambot.app.model.types.Entity;
import com.telegrambot.app.model.types.Reference;
import com.telegrambot.app.repositories.reference.TaskStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TaskStatusConverter extends Converter {

    private final Class<TaskStatus> classType = TaskStatus.class;
    private final TaskStatusRepository repository;

    @Override
    public <T extends Entity, R extends EntityResponse> R convertToResponse(T entity) {
        if (entity instanceof Reference ref) {
            return (R) convertReferenceToResponse(ref, TaskStatusResponse.class);
        }
        return null;
    }

    @Override
    public <T extends Entity, R extends EntityResponse> T updateEntity(R dto, T entity) {
        if (dto instanceof TaskStatusResponse response && entity instanceof TaskStatus entityBD) {
            entityBD.setName(response.getName());
            return entity;
        }
        return null;
    }

    @Override
    public <T extends Entity, R extends EntityResponse> T getOrCreateEntity(R dto) {
        return (T) Converter.getOrCreateEntity(dto, repository, classType);
    }

    @Override
    public <T extends Entity> T getOrCreateEntity(String guid, boolean isSaved) {
        return (T) Converter.getOrCreateEntity(guid, repository, classType, isSaved);
    }
}
