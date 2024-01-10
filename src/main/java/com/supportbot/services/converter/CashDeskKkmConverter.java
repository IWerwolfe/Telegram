package com.supportbot.services.converter;

import com.supportbot.DTO.api.reference.cashDeskKkm.CashDeskKkmResponse;
import com.supportbot.DTO.api.typeОbjects.EntityResponse;
import com.supportbot.model.reference.CashDeskKkm;
import com.supportbot.model.types.Entity;
import com.supportbot.model.types.Reference;
import com.supportbot.repositories.CashDeskKkmRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CashDeskKkmConverter extends Converter {

    private final Class<CashDeskKkm> classType = CashDeskKkm.class;
    private final CashDeskKkmRepository repository;

    @Override
    public <T extends Entity, R extends EntityResponse> R convertToResponse(T entity) {
        if (entity instanceof Reference ref) {
            return (R) convertReferenceToResponse(ref, CashDeskKkmResponse.class);
        }
        return null;
    }

    @Override
    public <T extends Entity, R extends EntityResponse> T updateEntity(R dto, T entity) {
        if (dto instanceof CashDeskKkmResponse response && entity instanceof CashDeskKkm entityBD) {
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
