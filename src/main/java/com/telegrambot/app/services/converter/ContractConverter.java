package com.telegrambot.app.services.converter;

import com.telegrambot.app.DTO.BillingType;
import com.telegrambot.app.DTO.api_1C.ContractResponse;
import com.telegrambot.app.model.documents.docdata.SyncData;
import com.telegrambot.app.model.legalentity.Contract;
import com.telegrambot.app.model.legalentity.LegalEntity;
import com.telegrambot.app.model.legalentity.Partner;
import com.telegrambot.app.repositories.ContractRepository;
import com.telegrambot.app.repositories.LegalEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ContractConverter extends Request1CConverter {

    private final ContractRepository contractRepository;
    private final LegalEntityRepository partnerRepository;

    @Override
    public <T, R> T updateEntity(R dto, T entity) {
        if (dto instanceof ContractResponse response && entity instanceof Contract entityBD) {
            entityBD.setName(response.getName());
            entityBD.setSyncData(new SyncData(response.getGuid(), response.getCode()));
            entityBD.setDate(convertToLocalDateTime(response.getDate()));
            entityBD.setBilling(convertToBoolean(response.getIsBilling()));
            entityBD.setStartBilling(convertToLocalDateTime(response.getStartBilling()));
            entityBD.setStopBilling(convertToLocalDateTime(response.getStopBilling()));
            entityBD.setStandardHourlyRate(response.getStandardHourlyRate());
            entityBD.setBillingType(convertToEnum(response.getBillingTypeString(), BillingType.class));
            entityBD.setPartner(getOrCreateEntity(response.getGuidPartner()));
            return (T) entity;
        }
        return null;
    }

    @Override
    public <T, R> T getOrCreateEntity(R dto) {
        if (dto instanceof ContractResponse response) {
            if (response.getGuid() == null || response.getGuid().isEmpty()) {
                return null;
            }
            Optional<Contract> existingEntity = contractRepository.findBySyncDataNotNullAndSyncData_Guid(response.getGuid());
            return (T) existingEntity.orElseGet(() -> new Contract(response.getGuid()));
        }
        return null;
    }

    protected Partner getOrCreateEntity(String guid) {
        Optional<LegalEntity> existingEntity = partnerRepository.findBySyncDataNotNullAndSyncData_Guid(guid);
        return (Partner) existingEntity.orElseGet(() -> partnerRepository.save(new Partner(guid)));
    }
}
