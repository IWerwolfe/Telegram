package com.telegrambot.app.services.converter;

import com.telegrambot.app.DTO.api_1C.taskResponse.TaskTypeResponse;
import com.telegrambot.app.DTO.api_1C.typesОbjects.Entity1C;
import com.telegrambot.app.DTO.types.TaskType;
import com.telegrambot.app.model.Entity;
import com.telegrambot.app.model.documents.docdata.SyncData;
import com.telegrambot.app.repositories.TaskTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TaskTypeConverter extends Converter1C {

    private final TaskTypeRepository typeRepository;

    @Override
    public <T extends Entity, R extends Entity1C> R convertToResponse(T entity) {
        return null;
    }

    @Override
    public <T extends Entity, R extends Entity1C> T updateEntity(R dto, T entity) {
        if (dto instanceof TaskTypeResponse response && entity instanceof TaskType entityBD) {
            entityBD.setSyncData(new SyncData(response.getGuid()));
            entityBD.setName(response.getName());
            return entity;
        }
        return null;
    }

    @Override
    public <T extends Entity, R extends Entity1C> T getOrCreateEntity(R dto) {
        return (T) Converter1C.getOrCreateEntity(dto, typeRepository, TaskType.class);
    }

    @Override
    public <T extends Entity> T getOrCreateEntity(String guid, boolean isSaved) {
        return (T) Converter1C.getOrCreateEntity(guid, typeRepository, TaskType.class, isSaved);
    }
}
