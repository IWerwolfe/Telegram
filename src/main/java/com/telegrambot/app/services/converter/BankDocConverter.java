package com.telegrambot.app.services.converter;

import com.telegrambot.app.DTO.api.doc.bankDoc.BankDocResponse;
import com.telegrambot.app.DTO.api.typeОbjects.EntityResponse;
import com.telegrambot.app.model.documents.doc.payment.BankDoc;
import com.telegrambot.app.model.types.Document;
import com.telegrambot.app.model.types.Entity;
import com.telegrambot.app.repositories.doc.BankDocRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BankDocConverter extends Converter {

    private final Class<BankDoc> classType = BankDoc.class;
    private final BankDocRepository repository;
    private final DataConverter dataConverter;
    private final ManagerConverter managerConverter;

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
            entityBD.setDivision(response.getGuidDivision());
            entityBD.setPartnerData(dataConverter.getPartnerData(response));

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
}
