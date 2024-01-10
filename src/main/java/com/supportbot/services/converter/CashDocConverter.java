package com.supportbot.services.converter;

import com.supportbot.DTO.api.doc.cashDoc.CashDocResponse;
import com.supportbot.DTO.api.typeОbjects.EntityResponse;
import com.supportbot.model.documents.doc.payment.CashDoc;
import com.supportbot.model.documents.docdata.FiscalData;
import com.supportbot.model.types.Document;
import com.supportbot.model.types.Entity;
import com.supportbot.repositories.doc.CashDocRepository;
import com.supportbot.repositories.reference.DivisionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CashDocConverter extends Converter {
    private final DivisionRepository divisionRepository;

    private final Class<CashDoc> classType = CashDoc.class;
    private final CashDocRepository repository;
    private final DataConverter dataConverter;
    private final ManagerConverter managerConverter;
    private final DivisionConverter divisionConverter;
    private final CompanyConverter companyConverter;

    @Override
    public <T extends Entity, R extends EntityResponse> R convertToResponse(T entity) {
        if (entity instanceof CashDoc entityBD) {
            CashDocResponse response = convertDocToResponse((Document) entityBD, CashDocResponse.class);
            dataConverter.fillPartnerDataToResponse(entityBD.getPartnerData(), response);
            response.setGuidCompany(convertToGuid(entityBD.getCompany()));

            if (entityBD.getFiscalData() != null) {
                FiscalData fiscal = entityBD.getFiscalData();
                response.setCashDeskKkm(convertToGuid(fiscal.getCashDeskKkm()));
                response.setCashShiftNumber(fiscal.getCashShiftNumber());
                response.setReceiptNumber(fiscal.getReceiptNumber());
                response.setCashShift(fiscal.getCashShift());
            }

            //TODO дописать конвертацию

            return (R) response;
        }
        return null;
    }

    @Override
    public <T extends Entity, R extends EntityResponse> T updateEntity(R dto, T entity) {
        if (dto instanceof CashDocResponse response && entity instanceof CashDoc entityBD) {
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
