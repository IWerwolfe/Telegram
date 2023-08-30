package com.telegrambot.app.services.converter;

import com.telegrambot.app.DTO.api.doc.taskDoc.TaskDocResponse;
import com.telegrambot.app.DTO.api.typeОbjects.Entity1C;
import com.telegrambot.app.model.Entity;
import com.telegrambot.app.model.documents.doc.service.TaskDoc;
import com.telegrambot.app.model.documents.docdata.PropertyData;
import com.telegrambot.app.model.documents.doctype.Document;
import com.telegrambot.app.repositories.doc.TaskDocRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TaskDocConverter extends Converter1C {

    private final Class<TaskDoc> classType = TaskDoc.class;
    private final TaskDocRepository repository;
    private final TaskTypeConverter typeConverter;
    private final TaskStatusConverter statusConverter;
    private final DataConverter dataConverter;
    private final ManagerConverter managerConverter;

    @Override
    public <T extends Entity, R extends Entity1C> R convertToResponse(T entity) {
        if (entity instanceof TaskDoc doc) {
            TaskDocResponse response = new TaskDocResponse();
            fillDocToResponse((Document) entity, response);
            dataConverter.fillPartnerDataToResponse(doc.getPartnerData(), response);
            response.setDescription(doc.getDescription());
            response.setDecision(doc.getDecision());
            response.setGuidStatus(convertToGuid(doc.getStatus()));
            response.setClosingDate(convertToDate(doc.getClosingDate()));
            response.setType(doc.getType().getName());
            response.setHighPriority(doc.getProperties().getHighPriority());
            response.setIsOutsourcing(doc.getProperties().getIsOutsourcing());
            response.setIsBilling(doc.getProperties().getIsBilling());
            return (R) response;
        }
        return null;
    }

    @Override
    public <T extends Entity, R extends Entity1C> T updateEntity(R dto, T entity) {
        if (dto instanceof TaskDocResponse response && entity instanceof TaskDoc entityBD) {
//            entityBD.setName(response.getName());
            fillResponseToDoc(entityBD, response);
            entityBD.setAuthor(response.getGuidAuthor()); //TODO заменить потом на сущность
            entityBD.setManager(managerConverter.getOrCreateEntity(response.getGuidManager(), true));
            entityBD.setDate(convertToLocalDateTime(response.getDate()));
            entityBD.setDescription(response.getDescription());
            entityBD.setDecision(response.getDecision());
            entityBD.setDivision(response.getGuidDivision());
            entityBD.setStatus(statusConverter.getOrCreateEntity(response.getGuidStatus(), true));
            entityBD.setPartnerData(dataConverter.getPartnerData(response));
            entityBD.setClosingDate(convertToLocalDateTime(response.getClosingDate()));
            entityBD.setType(typeConverter.getOrCreateEntity(response.getType(), true));
            entityBD.setProperties(getProperties(response));
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
    public <T extends Entity, R extends Entity1C> T getOrCreateEntity(R dto) {
        return (T) Converter1C.getOrCreateEntity(dto, repository, classType);
    }

    @Override
    public <T extends Entity> T getOrCreateEntity(String guid, boolean isSaved) {
        return (T) Converter1C.getOrCreateEntity(guid, repository, classType);
    }
}
