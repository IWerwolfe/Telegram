package com.telegrambot.app.services.converter;

import com.telegrambot.app.DTO.api.doc.bankDoc.BankDocResponse;
import com.telegrambot.app.DTO.api.typeОbjects.Entity1C;
import com.telegrambot.app.model.Entity;
import com.telegrambot.app.model.documents.doc.payment.BankDoc;
import com.telegrambot.app.model.documents.doctype.Document;
import com.telegrambot.app.repositories.doc.BankDocRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BankDocConverter extends Converter1C {

    private final Class<BankDoc> classType = BankDoc.class;
    private final BankDocRepository repository;
    private final DataConverter dataConverter;
    private final ManagerConverter managerConverter;

    @Override
    public <T extends Entity, R extends Entity1C> R convertToResponse(T entity) {
        if (entity instanceof BankDoc entityBD) {
            BankDocResponse response = new BankDocResponse();
            fillDocToResponse((Document) entityBD, response);
            dataConverter.fillPartnerDataToResponse(entityBD.getPartnerData(), response);
            response.setGuidCompany(convertToGuid(entityBD.getCompany()));

            //TODO дописать конвертацию

            return (R) response;
        }
        return null;
    }

    @Override
    public <T extends Entity, R extends Entity1C> T updateEntity(R dto, T entity) {
        if (dto instanceof BankDocResponse response && entity instanceof BankDoc entityBD) {
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
