package com.supportbot.services.converter;

import com.supportbot.DTO.api.doc.taskDoc.TaskDocResponse;
import com.supportbot.DTO.api.typeObjects.EntityResponse;
import com.supportbot.model.documents.doc.service.TaskDoc;
import com.supportbot.model.documents.docdata.PropertyData;
import com.supportbot.model.types.Document;
import com.supportbot.model.types.Entity;
import com.supportbot.repositories.doc.TaskDocRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class TaskDocConverter extends Converter {
    private final Class<TaskDoc> classType = TaskDoc.class;
    private final TaskDocRepository repository;
    private final TaskTypeConverter typeConverter;
    private final TaskStatusConverter statusConverter;
    private final DataConverter dataConverter;
    private final ManagerConverter managerConverter;
    private final DivisionConverter divisionConverter;
    private final CompanyConverter companyConverter;

    @Override
    public <T extends Entity, R extends EntityResponse> R convertToResponse(T entity) {
        if (entity instanceof TaskDoc entityBD) {
            TaskDocResponse response = convertDocToResponse((Document) entityBD, TaskDocResponse.class);
            response.setDescription(entityBD.getDescription());
            response.setDecision(entityBD.getDecision());
            response.setGuidStatus(convertToGuid(entityBD.getStatus()));
            response.setClosingDate(convertToDate(entityBD.getClosingDate()));
            response.setType(convertToGuid(entityBD.getType()));
            response.setHighPriority(entityBD.getProperties().getHighPriority());
            response.setIsOutsourcing(entityBD.getProperties().getIsOutsourcing());
            response.setIsBilling(entityBD.getProperties().getIsBilling());
            return (R) response;
        }
        return null;
    }

    @Override
    public <T extends Entity, R extends EntityResponse> T updateEntity(R dto, T entity) {
        if (dto instanceof TaskDocResponse response && entity instanceof TaskDoc entityBD) {
//            entityBD.setName(response.getName());
            fillResponseToDoc(entityBD, response);
            entityBD.setAuthor(response.getGuidAuthor()); //TODO заменить потом на сущность
            entityBD.setManager(managerConverter.getOrCreateEntity(response.getGuidManager(), true));
            entityBD.setDate(convertToLocalDateTime(response.getDate()));
            entityBD.setDescription(response.getDescription());
            entityBD.setDecision(response.getDecision());
            entityBD.setDivision(divisionConverter.getOrCreateEntity(response.getGuidDivision(), true));
            entityBD.setStatus(statusConverter.getOrCreateEntity(response.getGuidStatus(), true));
            entityBD.setPartnerData(dataConverter.getPartnerData(response));
            entityBD.setClosingDate(convertToLocalDateTime(response.getClosingDate()));
            entityBD.setType(typeConverter.getOrCreateEntity(response.getType(), true));
            entityBD.setProperties(getProperties(response));
            entityBD.setCompany(companyConverter.getOrCreateEntity(response.getGuidCompany(), true));
            return entity;
        }
        return null;
    }

    private PropertyData getProperties(TaskDocResponse taskDocResponse) {
        PropertyData properties = new PropertyData();
        properties.setHighPriority(convertToBoolean(taskDocResponse.getHighPriority()));
        properties.setIsOutsourcing(convertToBoolean(taskDocResponse.getIsOutsourcing()));
        properties.setIsBilling(convertToBoolean(taskDocResponse.getIsBilling()));
        return properties;
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
