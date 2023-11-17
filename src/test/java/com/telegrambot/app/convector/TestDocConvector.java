package com.telegrambot.app.convector;

import com.telegrambot.app.DTO.api.typeОbjects.EntityResponse;
import com.telegrambot.app.mockResponse.testDoc.TestDocResponse;
import com.telegrambot.app.model.types.Document;
import com.telegrambot.app.model.types.Entity;
import com.telegrambot.app.moskModel.TestDoc;
import com.telegrambot.app.repository.TestDocRepository;
import com.telegrambot.app.services.converter.Converter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@ExtendWith(MockitoExtension.class)
public class TestDocConvector extends Converter {

    @Mock
    private final Class<TestDoc> classType = TestDoc.class;
    @Mock
    private final TestDocRepository repository;

    @Override
    public <T extends Entity, R extends EntityResponse> R convertToResponse(T entity) {
        if (entity instanceof TestDoc entityBD) {
            TestDocResponse response = convertDocToResponse((Document) entityBD, TestDocResponse.class);
            return (R) response;
        }
        return null;
    }

    @Override
    public <T extends Entity, R extends EntityResponse> T updateEntity(R dto, T entity) {
        if (dto instanceof TestDocResponse response && entity instanceof TestDoc entityBD) {
//            entityBD.setName(response.getName());
            fillResponseToDoc(entityBD, response);
            entityBD.setAuthor(response.getGuidAuthor()); //TODO заменить потом на сущность
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
