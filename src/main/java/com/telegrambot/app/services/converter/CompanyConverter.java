package com.telegrambot.app.services.converter;

import com.telegrambot.app.DTO.api.legal.company.CompanyResponse;
import com.telegrambot.app.DTO.api.typeОbjects.Entity1C;
import com.telegrambot.app.model.Entity;
import com.telegrambot.app.model.legalentity.Company;
import com.telegrambot.app.repositories.CompanyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CompanyConverter extends Converter1C {

    private final Class<Company> classType = Company.class;
    private final CompanyRepository repository;

    @Override
    public <T extends Entity, R extends Entity1C> R convertToResponse(T entity) {
        if (entity instanceof Company entityBD) {
            CompanyResponse response = convertReferenceToResponse(entityBD);
            response.setInn(entityBD.getInn());
            response.setKpp(entityBD.getKpp());
            response.setGuidBankAccount(entityBD.getBankAccount());
            response.setComment(entityBD.getComment());
            response.setOgrn(entityBD.getOGRN());
            response.setCommencement(convertToDate(entityBD.getCommencement()));
            response.setCertificate(entityBD.getCertificate());
            response.setDateCertificate(convertToDate(entityBD.getDateCertificate()));
            response.setOkpo(entityBD.getOKPO());
            response.setGuidDefaultContract(convertToGuid(entityBD.getDefaultContract()));
            return (R) response;
        }
        return null;
    }

    @Override
    public <T extends Entity, R extends Entity1C> T updateEntity(R dto, T entity) {
        if (dto instanceof CompanyResponse response && entity instanceof Company entityBD) {
            entityBD.setName(response.getName());
            entityBD.setInn(response.getInn());
            entityBD.setKpp(response.getKpp());
            entityBD.setBankAccount(response.getGuidBankAccount());
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
    public <T extends Entity, R extends Entity1C> T getOrCreateEntity(R dto) {
        return (T) Converter1C.getOrCreateEntity(dto, repository, classType);
    }

    @Override
    public <T extends Entity> T getOrCreateEntity(String guid, boolean isSaved) {
        return (T) Converter1C.getOrCreateEntity(guid, repository, classType, isSaved);
    }
}
