package com.telegrambot.app.services.converter;

import com.telegrambot.app.DTO.api.reference.cashDesk.CashDeskResponse;
import com.telegrambot.app.DTO.api.typeОbjects.EntityResponse;
import com.telegrambot.app.model.reference.CashDesk;
import com.telegrambot.app.model.types.Entity;
import com.telegrambot.app.model.types.Reference;
import com.telegrambot.app.repositories.CashDeskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CashDeskConverter extends Converter {

    private final Class<CashDesk> classType = CashDesk.class;
    private final CashDeskRepository repository;

    @Override
    public <T extends Entity, R extends EntityResponse> R convertToResponse(T entity) {
        if (entity instanceof Reference ref) {
            return (R) convertReferenceToResponse(ref, CashDeskResponse.class);
        }
        return null;
    }

    @Override
    public <T extends Entity, R extends EntityResponse> T updateEntity(R dto, T entity) {
        if (dto instanceof CashDeskResponse response && entity instanceof CashDesk entityBD) {
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

    @Override
    public <T extends Entity, S extends EntityResponse> List<T> convertToEntityList(List<S> list, boolean isSaved) {
        return (List<T>) (isSaved ? convertToEntityListIsSave(list, this, repository) :
                convertToEntityList(list, this));
    }
}