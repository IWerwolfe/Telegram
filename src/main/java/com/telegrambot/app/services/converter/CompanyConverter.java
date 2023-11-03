package com.telegrambot.app.services.converter;

import com.telegrambot.app.DTO.api.reference.legal.company.CompanyResponse;
import com.telegrambot.app.DTO.api.typeОbjects.EntityResponse;
import com.telegrambot.app.model.reference.legalentity.Company;
import com.telegrambot.app.model.types.Entity;
import com.telegrambot.app.repositories.command.CompanyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CompanyConverter extends Converter {

    private final Class<Company> classType = Company.class;
    private final CompanyRepository repository;
    private final BankAccountConverter bankAccountConverter;

    @Override
    public <T extends Entity, R extends EntityResponse> R convertToResponse(T entity) {
        if (entity instanceof Company entityBD) {
            CompanyResponse response = convertReferenceToResponse(entityBD, CompanyResponse.class);
            response.setInn(entityBD.getInn());
            response.setKpp(entityBD.getKpp());
            response.setGuidBankAccount(convertToGuid(entityBD.getBankAccount()));
            response.setComment(entityBD.getComment());
            response.setOgrn(entityBD.getOGRN());
            response.setCommencement(convertToDate(entityBD.getCommencement()));
            response.setCertificate(entityBD.getCertificate());
            response.setDateCertificate(convertToDate(entityBD.getDateCertificate()));
            response.setOkpo(entityBD.getOKPO());
            return (R) response;
        }
        return null;
    }

    @Override
    public <T extends Entity, R extends EntityResponse> T updateEntity(R dto, T entity) {
        if (dto instanceof CompanyResponse response && entity instanceof Company entityBD) {
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
