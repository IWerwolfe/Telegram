package com.supportbot.services.converter;

import com.supportbot.DTO.api.doc.cardDoc.CardDocResponse;
import com.supportbot.DTO.api.typeОbjects.EntityResponse;
import com.supportbot.DTO.types.PaymentType;
import com.supportbot.model.documents.doc.payment.CardDoc;
import com.supportbot.model.documents.docdata.FiscalData;
import com.supportbot.model.types.Document;
import com.supportbot.model.types.Entity;
import com.supportbot.repositories.doc.CardDocRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CardDocConverter extends Converter {

    private final Class<CardDoc> classType = CardDoc.class;
    private final CardDocRepository repository;
    private final DataConverter dataConverter;
    private final ManagerConverter managerConverter;
    private final DivisionConverter divisionConverter;
    private final CompanyConverter companyConverter;
    private final BankAccountConverter bankAccountConverter;
    private final PayTerminalConverter payTerminalConverter;
    private final CashDeskConverter cashDeskConverter;
    private final CashDeskKkmConverter cashDeskKkmConverter;

    @Override
    public <T extends Entity, R extends EntityResponse> R convertToResponse(T entity) {
        if (entity instanceof CardDoc entityBD) {
            CardDocResponse response = convertDocToResponse((Document) entityBD, CardDocResponse.class);
            response.setGuidBankAccount(convertToGuid(entityBD.getBankAccount()));
            response.setGuidPayTerminal(convertToGuid(entityBD.getPayTerminal()));
            response.setGuidCashDesk(convertToGuid(entityBD.getCashDesk()));
            response.setTicketNumber(entityBD.getTicketNumber());
            response.setCommissionPercentage(String.valueOf(entityBD.getCommissionPercentage()));
            response.setReferenceNumber(entityBD.getReferenceNumber());
            response.setCommission(String.valueOf(entityBD.getCommission()));
            response.setPaymentTypeString(entityBD.getPaymentType().name());

            if (entityBD.getCardData() != null) {
                response.setCardNumber(entityBD.getCardData().getCardNumber());
                response.setCardType(entityBD.getCardData().getCardType());
            }
            if (entityBD.getFiscalData() != null) {
                FiscalData fiscal = entityBD.getFiscalData();
                response.setGuidCashDeskKkm(convertToGuid(fiscal.getCashDeskKkm()));
                response.setCashShiftNumber(fiscal.getCashShiftNumber());
                response.setReceiptNumber(fiscal.getReceiptNumber());
                response.setGuidCashShift(fiscal.getCashShift());
            }

            return (R) response;
        }
        return null;
    }

    @Override
    public <T extends Entity, R extends EntityResponse> T updateEntity(R dto, T entity) {
        if (dto instanceof CardDocResponse response && entity instanceof CardDoc entityBD) {
            fillResponseToDoc(entityBD, response);
            entityBD.setManager(managerConverter.getOrCreateEntity(response.getGuidManager(), true));
            entityBD.setDivision(divisionConverter.getOrCreateEntity(response.getGuidDivision(), true));
            entityBD.setCompany(companyConverter.getOrCreateEntity(response.getGuidCompany(), true));
            entityBD.setAuthor(response.getGuidAuthor());

            entityBD.setBankAccount(bankAccountConverter.getOrCreateEntity(response.getGuidBankAccount(), true));
            entityBD.setPayTerminal(payTerminalConverter.getOrCreateEntity(response.getGuidPayTerminal(), true));
            entityBD.setCashDesk(cashDeskConverter.getOrCreateEntity(response.getGuidCashDesk(), true));
            entityBD.setTicketNumber(response.getTicketNumber());
            entityBD.setCommissionPercentage(Long.valueOf(response.getCommissionPercentage()));
            entityBD.setReferenceNumber(response.getReferenceNumber());
            entityBD.setCommission(Integer.valueOf(response.getCommission()));
            entityBD.setPaymentType(convertToEnum(response.getPaymentTypeString(), PaymentType.class));

            entityBD.setPartnerData(dataConverter.getPartnerData(response));
            entityBD.setFiscalData(dataConverter.getFiscalData(response));
            entityBD.setCardData(dataConverter.getCardData(response));

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
