package com.supportbot.services.converter;

import com.supportbot.DTO.api.reference.taskType.TaskTypeResponse;
import com.supportbot.DTO.api.typeОbjects.EntityResponse;
import com.supportbot.DTO.types.TaskType;
import com.supportbot.model.types.Entity;
import com.supportbot.model.types.Reference;
import com.supportbot.repositories.reference.TaskTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class TaskTypeConverter extends Converter {

    private final Class<TaskType> classType = TaskType.class;
    private final TaskTypeRepository repository;

    @Override
    public <T extends Entity, R extends EntityResponse> R convertToResponse(T entity) {
        if (entity instanceof Reference ref) {
            return (R) convertReferenceToResponse(ref, TaskTypeResponse.class);
        }
        return null;
    }

    @Override
    public <T extends Entity, R extends EntityResponse> T updateEntity(R dto, T entity) {
        if (dto instanceof TaskTypeResponse response && entity instanceof TaskType entityBD) {
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
        return (T) getOrCreateEntity(guid, repository, classType, isSaved);
    }

    @Override
    public <T extends Entity, S extends EntityResponse> List<T> convertToEntityList(List<S> list, boolean isSaved) {
        return (List<T>) (isSaved ? convertToEntityListIsSave(list, this, repository) :
                convertToEntityList(list, this));
    }
}
