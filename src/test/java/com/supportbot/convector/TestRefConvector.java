package com.supportbot.convector;

import com.supportbot.DTO.api.typeObjects.EntityResponse;
import com.supportbot.mockResponse.testDoc.TestRefResponse;
import com.supportbot.model.types.Entity;
import com.supportbot.moskModel.TestRef;
import com.supportbot.repository.TestRefRepository;
import com.supportbot.services.converter.Converter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Component
public class TestRefConvector extends Converter {

    private final Class<TestRef> classType = TestRef.class;
    private final TestRefRepository repository;

    @Override
    public <T extends Entity, R extends EntityResponse> R convertToResponse(T entity) {
        if (entity instanceof TestRef entityBD) {
            TestRefResponse response = convertReferenceToResponse(entityBD, TestRefResponse.class);
            return (R) response;
        }
        return null;
    }

    @Override
    public <T extends Entity, R extends EntityResponse> T updateEntity(R dto, T entity) {
        if (dto instanceof TestRefResponse response && entity instanceof TestRef entityBD) {
            entityBD.setName(response.getName());
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
        return (T) Converter.getOrCreateEntity(guid, repository, classType, isSaved);
    }

    @Override
    public <T extends Entity, S extends EntityResponse> List<T> convertToEntityList(List<S> list, boolean isSaved) {
        return (List<T>) (isSaved ? convertToEntityListIsSave(list, this, repository) :
                convertToEntityList(list, this));
    }
}
