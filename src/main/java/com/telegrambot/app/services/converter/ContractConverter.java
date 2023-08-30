package com.telegrambot.app.services.converter;

import com.telegrambot.app.DTO.api.legal.contract.ContractResponse;
import com.telegrambot.app.DTO.api.typeОbjects.Entity1C;
import com.telegrambot.app.DTO.types.BillingType;
import com.telegrambot.app.model.Entity;
import com.telegrambot.app.model.legalentity.Contract;
import com.telegrambot.app.repositories.reference.ContractRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ContractConverter extends Converter1C {

    private final Class<Contract> classType = Contract.class;
    private final ContractRepository repository;
    private final PartnerConverter legalConverter;

    @Override
    public <T extends Entity, R extends Entity1C> R convertToResponse(T entity) {
        if (entity instanceof Contract entityBD) {
            ContractResponse response = convertReferenceToResponse(entityBD);
            response.setGuidPartner(convertToGuid(entityBD.getPartner()));
            response.setStartBilling(convertToDate(entityBD.getStartBilling()));
            response.setDate(convertToDate(entityBD.getDate()));
            response.setStopBilling(convertToDate(entityBD.getStopBilling()));
            response.setStandardHourlyRate(String.valueOf(entityBD.getStandardHourlyRate()));
            response.setIsBilling(convertToBoolean(entityBD.getIsBilling()));
            response.setBillingTypeString(String.valueOf(entityBD.getBillingType()));
            return (R) response;
        }
        return null;
    }

    @Override
    public <T extends Entity, R extends Entity1C> T updateEntity(R dto, T entity) {
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
    public <T extends Entity, R extends Entity1C> T getOrCreateEntity(R dto) {
        return (T) Converter1C.getOrCreateEntity(dto, repository, classType);
    }

    @Override
    public <T extends Entity> T getOrCreateEntity(String guid, boolean isSaved) {
        return (T) Converter1C.getOrCreateEntity(guid, repository, classType, isSaved);
    }
}
