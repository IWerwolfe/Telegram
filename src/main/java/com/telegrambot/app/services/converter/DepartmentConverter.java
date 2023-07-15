package com.telegrambot.app.services.converter;

import com.telegrambot.app.DTO.DepartmentResponse;
import com.telegrambot.app.model.documents.docdata.SyncData;
import com.telegrambot.app.model.legalentity.Contract;
import com.telegrambot.app.model.legalentity.Department;
import com.telegrambot.app.model.legalentity.LegalEntity;
import com.telegrambot.app.model.legalentity.Partner;
import com.telegrambot.app.repositories.ContractRepository;
import com.telegrambot.app.repositories.DepartmentRepository;
import com.telegrambot.app.repositories.LegalEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class DepartmentConverter extends Request1CConverter {

    private final DepartmentRepository departmentRepository;
    private final LegalEntityRepository partnerRepository;
    private final ContractRepository contractRepository;

    @Override
    public <T, R> T updateEntity(R dto, T entity) {
        if (dto instanceof DepartmentResponse response && entity instanceof Department entityBD) {
            entityBD.setName(response.getName());
            entityBD.setSyncData(new SyncData(response.getGuid(), response.getCode()));
            entityBD.setBilling(convertToBoolean(response.getIsBilling()));
            entityBD.setExcusableGoods(convertToBoolean(response.getIsExcusableGoods()));
            entityBD.setMarkedGoods(convertToBoolean(response.getIsMarkedGoods()));
            entityBD.setEGAIS(convertToBoolean(response.getIsEGAIS()));
            entityBD.setPartner(getOrCreateEntityPartner(response.getGuidPartner()));
            entityBD.setContract(getOrCreateEntityContract(response.getGuidContract()));
            return (T) entityBD;
        }
        return null;
    }

    @Override
    public <T, R> T getOrCreateEntity(R dto) {
        if (dto instanceof DepartmentResponse response) {
            if (response.getGuid() == null || response.getGuid().isEmpty()) {
                return null;
            }
            Optional<Department> existingEntity = departmentRepository.findBySyncDataNotNullAndSyncData_Guid(response.getGuid());
            return (T) existingEntity.orElseGet(() -> new Department(response.getGuid()));
        }
        return null;
    }

    protected Partner getOrCreateEntityPartner(String guid) {
        Optional<LegalEntity> existingEntity = partnerRepository.findBySyncDataNotNullAndSyncData_Guid(guid);
        return (Partner) existingEntity.orElseGet(() -> partnerRepository.save(new Partner(guid)));
    }

    protected Contract getOrCreateEntityContract(String guid) {
        Optional<Contract> existingEntity = contractRepository.findBySyncDataNotNullAndSyncData_Guid(guid);
        return existingEntity.orElseGet(() -> contractRepository.save(new Contract(guid)));
    }
}

