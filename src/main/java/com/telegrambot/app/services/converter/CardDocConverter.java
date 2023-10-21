package com.telegrambot.app.services.converter;

import com.telegrambot.app.DTO.api.doc.cardDoc.CardDocResponse;
import com.telegrambot.app.DTO.api.typeОbjects.EntityResponse;
import com.telegrambot.app.model.documents.doc.payment.CardDoc;
import com.telegrambot.app.model.documents.docdata.FiscalData;
import com.telegrambot.app.model.types.Document;
import com.telegrambot.app.model.types.Entity;
import com.telegrambot.app.repositories.doc.CardDocRepository;
import com.telegrambot.app.repositories.reference.DivisionRepository;
import com.telegrambot.app.repositories.reference.PayTerminalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CardDocConverter extends Converter {
    private final DivisionRepository divisionRepository;
    private final PayTerminalRepository payTerminalRepository;

    private final Class<CardDoc> classType = CardDoc.class;
    private final CardDocRepository repository;
    private final DataConverter dataConverter;
    private final ManagerConverter managerConverter;
    private final DivisionConverter divisionConverter;

    @Override
    public <T extends Entity, R extends EntityResponse> R convertToResponse(T entity) {
        if (entity instanceof CardDoc entityBD) {
            CardDocResponse response = convertDocToResponse((Document) entityBD, CardDocResponse.class);
            dataConverter.fillPartnerDataToResponse(entityBD.getPartnerData(), response);
            response.setGuidCompany(convertToGuid(entityBD.getCompany()));
            response.setGuidBankAccount(convertToGuid(entityBD.getBankAccount()));
            response.setPaymentTerminal(convertToGuid(entityBD.getPayTerminal()));
            response.setTicketNumber(String.valueOf(entityBD.getTicketNumber()));
            response.setCommissionPercentage(String.valueOf(entityBD.getCommissionPercentage()));
            response.setReferenceNumber(entityBD.getReferenceNumber());
            response.setCommission(String.valueOf(entityBD.getCommission()));
            if (entityBD.getCardData() != null) {
                response.setCardNumber(entityBD.getCardData().getCardNumber());
                response.setCardType(entityBD.getCardData().getCardType());
            }
            if (entityBD.getFiscalData() != null) {
                FiscalData fiscal = entityBD.getFiscalData();
                response.setCashDeskKkm(fiscal.getCashDeskKkm());
                response.setCashShiftNumber(fiscal.getCashShiftNumber());
                response.setReceiptNumber(fiscal.getReceiptNumber());
                response.setCashShift(fiscal.getCashShift());
            }

            return (R) response;
        }
        return null;
    }

    @Override
    public <T extends Entity, R extends EntityResponse> T updateEntity(R dto, T entity) {
        if (dto instanceof CardDocResponse response && entity instanceof CardDoc entityBD) {
//            entityBD.setName(response.getName());
            fillResponseToDoc(entityBD, response);
            entityBD.setManager(managerConverter.getOrCreateEntity(response.getGuidManager(), true));
            entityBD.setDate(convertToLocalDateTime(response.getDate()));
            entityBD.setDivision(divisionConverter.getOrCreateEntity(response.getGuidDivision(), true));
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

    @Override
    public <T extends Entity, S extends EntityResponse> List<T> convertToEntityList(List<S> list, boolean isSaved) {
        return (List<T>) (isSaved ? convertToEntityListIsSave(list, this, repository) :
                convertToEntityList(list, this));
    }
}
