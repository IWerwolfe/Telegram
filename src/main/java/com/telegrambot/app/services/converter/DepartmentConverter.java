package com.telegrambot.app.services.converter;

import com.telegrambot.app.DTO.api_1C.legal.partner.DepartmentResponse;
import com.telegrambot.app.DTO.api_1C.typeОbjects.Entity1C;
import com.telegrambot.app.model.Entity;
import com.telegrambot.app.model.legalentity.Department;
import com.telegrambot.app.repositories.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DepartmentConverter extends Converter1C {

    private final Class<Department> classType = Department.class;
    private final DepartmentRepository repository;
    private final ContractConverter contractConverter;
    private final PartnerConverter legalConverter;

    @Override
    public <T extends Entity, R extends Entity1C> R convertToResponse(T entity) {
        return null;
    }

    @Override
    public <T extends Entity, R extends Entity1C> T updateEntity(R dto, T entity) {
        if (dto instanceof DepartmentResponse response && entity instanceof Department entityBD) {
            entityBD.setName(response.getName());
            entityBD.setBilling(convertToBoolean(response.getIsBilling()));
            entityBD.setExcusableGoods(convertToBoolean(response.getIsExcusableGoods()));
            entityBD.setMarkedGoods(convertToBoolean(response.getIsMarkedGoods()));
            entityBD.setEGAIS(convertToBoolean(response.getIsEGAIS()));
            entityBD.setPartner(legalConverter.getOrCreateEntity(response.getGuidPartner(), true));
            entityBD.setContract(contractConverter.getOrCreateEntity(response.getGuidContract(), true));
            return (T) entityBD;
        }
        return null;
    }

    @Override
    public <T extends Entity, R extends Entity1C> T getOrCreateEntity(R dto) {
        return (T) Converter1C.getOrCreateEntity(dto, repository, classType);
    }

    @Override
    public <T extends Entity> T getOrCreateEntity(String guid, boolean isSaved) {
        return (T) Converter1C.getOrCreateEntity(guid, repository, classType, isSaved);
    }
}

