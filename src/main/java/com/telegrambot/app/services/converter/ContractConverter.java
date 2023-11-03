package com.telegrambot.app.services.converter;

import com.telegrambot.app.DTO.api.reference.legal.contract.ContractResponse;
import com.telegrambot.app.DTO.api.typeОbjects.EntityResponse;
import com.telegrambot.app.DTO.types.BillingType;
import com.telegrambot.app.model.reference.legalentity.Contract;
import com.telegrambot.app.model.types.Entity;
import com.telegrambot.app.repositories.reference.ContractRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ContractConverter extends Converter {

    private final Class<Contract> classType = Contract.class;
    private final ContractRepository repository;
    private final PartnerConverter legalConverter;

    @Override
    public <T extends Entity, R extends EntityResponse> R convertToResponse(T entity) {
        if (entity instanceof Contract entityBD) {
            ContractResponse response = convertReferenceToResponse(entityBD, ContractResponse.class);
            response.setGuidPartner(convertToGuid(entityBD.getPartner()));
            response.setStartBilling(convertToDate(entityBD.getStartBilling()));
            response.setDate(convertToDate(entityBD.getDate()));
            response.setStopBilling(convertToDate(entityBD.getStopBilling()));
            response.setStandardHourlyRate(String.valueOf(entityBD.getStandardHourlyRate()));
            response.setIsBilling(convertToBoolean(entityBD.getIsBilling()));
            response.setBillingTypeString(entityBD.getBillingType().name());
            return (R) response;
        }
        return null;
    }

    @Override
    public <T extends Entity, R extends EntityResponse> T updateEntity(R dto, T entity) {
        if (dto instanceof ContractResponse response && entity instanceof Contract entityBD) {
            entityBD.setName(response.getName());
            entityBD.setDate(convertToLocalDateTime(response.getDate()));
            entityBD.setIsBilling(convertToBoolean(response.getIsBilling()));
            entityBD.setStartBilling(convertToLocalDateTime(response.getStartBilling()));
            entityBD.setStopBilling(convertToLocalDateTime(response.getStopBilling()));
            entityBD.setStandardHourlyRate(Integer.valueOf(response.getStandardHourlyRate()));
            entityBD.setBillingType(convertToEnum(response.getBillingTypeString(), BillingType.class));
            entityBD.setPartner(legalConverter.getOrCreateEntity(response.getGuidPartner(), true));
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
