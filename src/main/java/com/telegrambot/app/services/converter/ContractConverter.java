package com.telegrambot.app.services.converter;

import com.telegrambot.app.DTO.api_1C.legal.partner.ContractResponse;
import com.telegrambot.app.DTO.api_1C.typeОbjects.Entity1C;
import com.telegrambot.app.DTO.types.BillingType;
import com.telegrambot.app.model.Entity;
import com.telegrambot.app.model.legalentity.Contract;
import com.telegrambot.app.repositories.ContractRepository;
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
        return null;
    }

    @Override
    public <T extends Entity, R extends Entity1C> T updateEntity(R dto, T entity) {
        if (dto instanceof ContractResponse response && entity instanceof Contract entityBD) {
            entityBD.setName(response.getName());
            entityBD.setDate(convertToLocalDateTime(response.getDate()));
            entityBD.setBilling(convertToBoolean(response.getIsBilling()));
            entityBD.setStartBilling(convertToLocalDateTime(response.getStartBilling()));
            entityBD.setStopBilling(convertToLocalDateTime(response.getStopBilling()));
            entityBD.setStandardHourlyRate(response.getStandardHourlyRate());
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
