package com.telegrambot.app.services.converter;

import com.telegrambot.app.DTO.api.doc.cashDoc.CashDocResponse;
import com.telegrambot.app.DTO.api.typeОbjects.Entity1C;
import com.telegrambot.app.model.Entity;
import com.telegrambot.app.model.documents.doc.payment.CashDoc;
import com.telegrambot.app.model.documents.docdata.FiscalData;
import com.telegrambot.app.model.documents.doctype.Document;
import com.telegrambot.app.repositories.doc.CashDocRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CashDocConverter extends Converter1C {

    private final Class<CashDoc> classType = CashDoc.class;
    private final CashDocRepository repository;
    private final DataConverter dataConverter;
    private final ManagerConverter managerConverter;

    @Override
    public <T extends Entity, R extends Entity1C> R convertToResponse(T entity) {
        if (entity instanceof CashDoc entityBD) {
            CashDocResponse response = new CashDocResponse();
            fillDocToResponse((Document) entityBD, response);
            dataConverter.fillPartnerDataToResponse(entityBD.getPartnerData(), response);
            response.setGuidCompany(convertToGuid(entityBD.getCompany()));

            if (entityBD.getFiscalData() != null) {
                FiscalData fiscal = entityBD.getFiscalData();
                response.setCashDeskKkm(fiscal.getCashDeskKkm());
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
    public <T extends Entity, R extends Entity1C> T updateEntity(R dto, T entity) {
        if (dto instanceof CashDocResponse response && entity instanceof CashDoc entityBD) {
//            entityBD.setName(response.getName());
            fillResponseToDoc(entityBD, response);
            entityBD.setManager(managerConverter.getOrCreateEntity(response.getGuidManager(), true));
            entityBD.setDate(convertToLocalDateTime(response.getDate()));
            entityBD.setDivision(response.getGuidDivision());
            entityBD.setPartnerData(dataConverter.getPartnerData(response));

            //TODO дописать конвертацию

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
        return (T) Converter1C.getOrCreateEntity(guid, repository, classType);
    }
}
