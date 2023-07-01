package com.telegrambot.app.services.converter;

import com.telegrambot.app.DTO.DepartmentResponse;
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
            entityBD.setGuid(response.getGuid());
            entityBD.setCode(response.getCode());
            entityBD.setBilling(response.getIsBilling() != null ? response.getIsBilling() : false);
            entityBD.setExcusableGoods(response.getIsExcusableGoods() != null ? response.getIsExcusableGoods() : false);
            entityBD.setMarkedGoods(response.getIsMarkedGoods() != null ? response.getIsMarkedGoods() : false);
            entityBD.setEGAIS(response.getIsEGAIS() != null ? response.getIsEGAIS() : false);
            entityBD.setPartner(getOrCreateEntityPartner(response.getGuidPartner()));
            entityBD.setContract(getOrCreateEntityContract(response.getGuidContract()));
            return (T) entityBD;
        }
        return null;
    }

    @Override
    protected <T, R> T getOrCreateEntity(R dto) {
        if (dto instanceof DepartmentResponse response) {
            Optional<Department> existingEntity = departmentRepository.findByGuid(response.getGuid());
            return (T) existingEntity.orElseGet(Department::new);
        }
        return null;
    }

    protected Partner getOrCreateEntityPartner(String guid) {
        Optional<LegalEntity> existingEntity = partnerRepository.findByGuid(guid);
        return (Partner) existingEntity.orElseGet(() -> partnerRepository.save(new Partner(guid)));
    }

    protected Contract getOrCreateEntityContract(String guid) {
        Optional<Contract> existingEntity = contractRepository.findByGuid(guid);
        return existingEntity.orElseGet(() -> contractRepository.save(new Contract(guid)));
    }
}

