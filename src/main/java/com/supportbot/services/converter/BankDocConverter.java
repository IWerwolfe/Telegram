package com.supportbot.services.converter;

import com.supportbot.DTO.api.doc.bankDoc.BankDocResponse;
import com.supportbot.DTO.api.typeObjects.EntityResponse;
import com.supportbot.model.documents.doc.payment.BankDoc;
import com.supportbot.model.types.Document;
import com.supportbot.model.types.Entity;
import com.supportbot.repositories.doc.BankDocRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class BankDocConverter extends Converter {

    private final Class<BankDoc> classType = BankDoc.class;
    private final BankDocRepository repository;
    private final DataConverter dataConverter;
    private final ManagerConverter managerConverter;
    private final DivisionConverter divisionConverter;
    private final CompanyConverter companyConverter;

    @Override
    public <T extends Entity, R extends EntityResponse> R convertToResponse(T entity) {
        if (entity instanceof BankDoc entityBD) {
            BankDocResponse response = convertDocToResponse((Document) entityBD, BankDocResponse.class);
            dataConverter.fillPartnerDataToResponse(entityBD.getPartnerData(), response);
            response.setGuidCompany(convertToGuid(entityBD.getCompany()));

            //TODO дописать конвертацию

            return (R) response;
        }
        return null;
    }

    @Override
    public <T extends Entity, R extends EntityResponse> T updateEntity(R dto, T entity) {
        if (dto instanceof BankDocResponse response && entity instanceof BankDoc entityBD) {
//            entityBD.setName(response.getName());
            fillResponseToDoc(entityBD, response);
            entityBD.setManager(managerConverter.getOrCreateEntity(response.getGuidManager(), true));
            entityBD.setDate(convertToLocalDateTime(response.getDate()));
            entityBD.setDivision(divisionConverter.getOrCreateEntity(response.getGuidDivision(), true));
            entityBD.setPartnerData(dataConverter.getPartnerData(response));
            entityBD.setCompany(companyConverter.getOrCreateEntity(response.getGuidCompany(), true));

            //TODO дописать конвертацию

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
        return (T) Converter.getOrCreateEntity(guid, repository, classType);
    }

    @Override
    public <T extends Entity, S extends EntityResponse> List<T> convertToEntityList(List<S> list, boolean isSaved) {
        return (List<T>) (isSaved ? convertToEntityListIsSave(list, this, repository) :
                convertToEntityList(list, this));
    }
}
