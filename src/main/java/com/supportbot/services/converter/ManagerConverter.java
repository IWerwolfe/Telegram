package com.supportbot.services.converter;

import com.supportbot.DTO.api.reference.manager.ManagerResponse;
import com.supportbot.DTO.api.typeОbjects.EntityResponse;
import com.supportbot.model.reference.Manager;
import com.supportbot.model.types.Entity;
import com.supportbot.model.types.Reference;
import com.supportbot.repositories.reference.ManagerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ManagerConverter extends Converter {

    private final Class<Manager> classType = Manager.class;
    private final ManagerRepository repository;

    @Override
    public <T extends Entity, R extends EntityResponse> R convertToResponse(T entity) {
        if (entity instanceof Reference ref) {
            return (R) convertReferenceToResponse(ref, ManagerResponse.class);
        }
        return null;
    }

    @Override
    public <T extends Entity, R extends EntityResponse> T updateEntity(R dto, T entity) {
        if (dto instanceof ManagerResponse response && entity instanceof Manager entityBD) {
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
