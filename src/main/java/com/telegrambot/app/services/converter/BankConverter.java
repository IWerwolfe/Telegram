package com.telegrambot.app.services.converter;

import com.telegrambot.app.DTO.api.reference.bank.BankResponse;
import com.telegrambot.app.DTO.api.typeОbjects.EntityResponse;
import com.telegrambot.app.model.reference.Bank;
import com.telegrambot.app.model.types.Entity;
import com.telegrambot.app.repositories.BankRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class BankConverter extends Converter {

    private final Class<Bank> classType = Bank.class;
    private final BankRepository repository;

    @Override
    public <T extends Entity, R extends EntityResponse> R convertToResponse(T entity) {
        if (entity instanceof Bank entityBD) {
            BankResponse response = convertReferenceToResponse(entityBD, BankResponse.class);
            response.setCorrespondentAccount(String.valueOf(entityBD.getCorrespondentAccount()));
            response.setCity(entityBD.getCity());
            response.setAddress(entityBD.getAddress());
            response.setPhone(entityBD.getPhone());
            response.setSwift(entityBD.getSwift());
            response.setCountry(entityBD.getCountry());
            return (R) response;
        }
        return null;
    }

    @Override
    public <T extends Entity, R extends EntityResponse> T updateEntity(R dto, T entity) {
        if (dto instanceof BankResponse response && entity instanceof Bank entityBD) {
            entityBD.setName(response.getName());
            entityBD.setCorrespondentAccount(Long.parseLong(response.getCorrespondentAccount()));
            entityBD.setCity(response.getCity());
            entityBD.setAddress(response.getAddress());
            entityBD.setPhone(response.getPhone());
            entityBD.setSwift(response.getSwift());
            entityBD.setCountry(response.getCountry());
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
