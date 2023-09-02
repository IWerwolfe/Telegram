package com.telegrambot.app.services.converter;

import com.telegrambot.app.DTO.api.reference.legal.department.DepartmentResponse;
import com.telegrambot.app.DTO.api.typeОbjects.EntityResponse;
import com.telegrambot.app.model.reference.legalentity.Department;
import com.telegrambot.app.model.types.Entity;
import com.telegrambot.app.repositories.reference.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DepartmentConverter extends Converter {

    private final Class<Department> classType = Department.class;
    private final DepartmentRepository repository;
    private final ContractConverter contractConverter;
    private final PartnerConverter legalConverter;

    @Override
    public <T extends Entity, R extends EntityResponse> R convertToResponse(T entity) {
        if (entity instanceof Department entityBD) {
            DepartmentResponse response = convertReferenceToResponse(entityBD, DepartmentResponse.class);
            response.setGuidPartner(convertToGuid(entityBD.getPartner()));
            response.setIsBilling(convertToBoolean(entityBD.getIsBilling()));
            response.setGuidContract(convertToGuid(entityBD.getContract()));
            response.setIsExcisableGoods(convertToBoolean(entityBD.getIsExcisableGoods()));
            response.setIsMarkedGoods(convertToBoolean(entityBD.getIsMarkedGoods()));
            response.setIsEgais(convertToBoolean(entityBD.getIsEgais()));
            return (R) response;
        }
        return null;
    }

    @Override
    public <T extends Entity, R extends EntityResponse> T updateEntity(R dto, T entity) {
        if (dto instanceof DepartmentResponse response && entity instanceof Department entityBD) {
            entityBD.setName(response.getName());
            entityBD.setIsBilling(convertToBoolean(response.getIsBilling()));
            entityBD.setIsExcisableGoods(convertToBoolean(response.getIsExcisableGoods()));
            entityBD.setIsMarkedGoods(convertToBoolean(response.getIsMarkedGoods()));
            entityBD.setIsEgais(convertToBoolean(response.getIsEgais()));
            entityBD.setPartner(legalConverter.getOrCreateEntity(response.getGuidPartner(), true));
            entityBD.setContract(contractConverter.getOrCreateEntity(response.getGuidContract(), true));
            return (T) entityBD;
        }
        return null;
    }

    @Override
    public <T extends Entity, R extends EntityResponse> T getOrCreateEntity(R dto) {
        return (T) Converter.getOrCreateEntity(dto, repository, classType);
    }

    @Override
    public <T extends Entity> T getOrCreateEntity(String guid, boolean isSaved) {
        return (T) Converter.getOrCreateEntity(guid, repository, classType, isSaved);
    }
}

