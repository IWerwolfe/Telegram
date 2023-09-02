package com.telegrambot.app.services.converter;

import com.telegrambot.app.DTO.api.doc.cardDoc.CardDocResponse;
import com.telegrambot.app.DTO.api.typeОbjects.EntityResponse;
import com.telegrambot.app.model.documents.doc.payment.CardDoc;
import com.telegrambot.app.model.documents.docdata.FiscalData;
import com.telegrambot.app.model.types.Document;
import com.telegrambot.app.model.types.Entity;
import com.telegrambot.app.repositories.doc.CardDocRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CardDocConverter extends Converter {

    private final Class<CardDoc> classType = CardDoc.class;
    private final CardDocRepository repository;
    private final DataConverter dataConverter;
    private final ManagerConverter managerConverter;

    @Override
    public <T extends Entity, R extends EntityResponse> R convertToResponse(T entity) {
        if (entity instanceof CardDoc entityBD) {
            CardDocResponse response = convertDocToResponse((Document) entityBD, CardDocResponse.class);
            dataConverter.fillPartnerDataToResponse(entityBD.getPartnerData(), response);
            response.setGuidCompany(convertToGuid(entityBD.getCompany()));
            response.setGuidBankAccount(convertToGuid(entityBD.getBankAccount()));
            response.setPaymentTerminal(entityBD.getPaymentTerminal());
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
