package com.supportbot.services.converter;

import com.supportbot.DTO.api.reference.bankAccount.BankAccountResponse;
import com.supportbot.DTO.api.typeObjects.EntityResponse;
import com.supportbot.DTO.types.BankAccountType;
import com.supportbot.model.reference.BankAccount;
import com.supportbot.model.reference.legalentity.LegalEntity;
import com.supportbot.model.types.Entity;
import com.supportbot.repositories.BankAccountRepository;
import com.supportbot.repositories.reference.LegalEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class BankAccountConverter extends Converter {
    private final LegalEntityRepository legalEntityRepository;

    private final Class<BankAccount> classType = BankAccount.class;
    private final BankAccountRepository repository;
    private final BankConverter bankConverter;

    @Override
    public <T extends Entity, R extends EntityResponse> R convertToResponse(T entity) {
        if (entity instanceof BankAccount entityBD) {
            BankAccountResponse response = convertReferenceToResponse(entityBD, BankAccountResponse.class);
            response.setCurrency(entityBD.getCurrency());
            response.setGuidBank(convertToGuid(entityBD.getBank()));
            response.setGuidPaymentBank(convertToGuid(entityBD.getPaymentBank()));
            response.setNumber(entityBD.getNumber());
            response.setNameType(entityBD.getType().name());
            response.setCorrespondent(entityBD.getCorrespondent());
            response.setPaymentPurpose(entityBD.getPaymentPurpose());
            response.setPermitNumberAndDate(entityBD.getPermitNumberAndDate());
            response.setOpeningDate(convertToDate(entityBD.getOpeningDate()));
            response.setClosingDate(convertToDate(entityBD.getClosingDate()));
            response.setGuidLegal(convertToGuid(entityBD.getLegal()));
            return (R) response;
        }
        return null;
    }

    @Override
    public <T extends Entity, R extends EntityResponse> T updateEntity(R dto, T entity) {
        if (dto instanceof BankAccountResponse response && entity instanceof BankAccount entityBD) {
            entityBD.setName(response.getName());
            entityBD.setCurrency(response.getCurrency());
            entityBD.setBank(bankConverter.getOrCreateEntity(response.getGuidBank(), true));
            entityBD.setPaymentBank(bankConverter.getOrCreateEntity(response.getGuidPaymentBank(), true));
            entityBD.setNumber(response.getNumber());
            entityBD.setType(convertToEnum(response.getNameType(), BankAccountType.class));
            entityBD.setCorrespondent(response.getCorrespondent());
            entityBD.setPaymentPurpose(response.getPaymentPurpose());
            entityBD.setPermitNumberAndDate(response.getPermitNumberAndDate());
            entityBD.setOpeningDate(convertToLocalDateTime(response.getOpeningDate()));
            entityBD.setClosingDate(convertToLocalDateTime(response.getClosingDate()));
            entityBD.setLegal(getOrCreateEntity(response.getGuidLegal(), legalEntityRepository, LegalEntity.class, true));
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
