package com.telegrambot.app.services.converter;

import com.telegrambot.app.DTO.api.reference.legal.partner.PartnerResponse;
import com.telegrambot.app.DTO.api.typeОbjects.EntityResponse;
import com.telegrambot.app.DTO.types.PartnerType;
import com.telegrambot.app.model.reference.legalentity.Partner;
import com.telegrambot.app.model.types.Entity;
import com.telegrambot.app.repositories.reference.PartnerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class PartnerConverter extends Converter {

    private final Class<Partner> classType = Partner.class;
    private final PartnerRepository repository;
    private final BankAccountConverter bankAccountConverter;
    private final ContractConverter contractConverter;

    @Override
    public <T extends Entity, R extends EntityResponse> R convertToResponse(T entity) {
        if (entity instanceof Partner entityBD) {
            PartnerResponse response = convertReferenceToResponse(entityBD, PartnerResponse.class);
            response.setInn(entityBD.getInn());
            response.setKpp(entityBD.getKpp());
            response.setGuidBankAccount(convertToGuid(entityBD.getBankAccount()));
            response.setComment(entityBD.getComment());
            response.setOgrn(entityBD.getOGRN());
            response.setCommencement(convertToDate(entityBD.getCommencement()));
            response.setCertificate(entityBD.getCertificate());
            response.setDateCertificate(convertToDate(entityBD.getDateCertificate()));
            response.setOkpo(entityBD.getOKPO());
            response.setGuidDefaultContract(convertToGuid(entityBD.getDefaultContract()));
            response.setPartnerTypeString(entityBD.getPartnerType().toString());
            return (R) response;
        }
        return null;
    }

    @Override
    public <T extends Entity, R extends EntityResponse> T updateEntity(R dto, T entity) {
        if (dto instanceof PartnerResponse response && entity instanceof Partner entityBD) {
            entityBD.setName(response.getName());
            entityBD.setInn(response.getInn());
            entityBD.setKpp(response.getKpp());
            entityBD.setBankAccount(bankAccountConverter.getOrCreateEntity(response.getGuidBankAccount(), true));
            entityBD.setComment(response.getComment());
            entityBD.setOGRN(response.getOgrn());
            entityBD.setCommencement(convertToLocalDateTime(response.getCommencement()));
            entityBD.setCertificate(response.getCertificate());
            entityBD.setDateCertificate(convertToLocalDateTime(response.getDateCertificate()));
            entityBD.setOKPO(response.getOkpo());
            entityBD.setPartnerType(convertToEnum(response.getPartnerTypeString(), PartnerType.class));
            entityBD.setDefaultContract(contractConverter.getOrCreateEntity(response.getGuidDefaultContract(), true));
            return (T) entityBD;
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
