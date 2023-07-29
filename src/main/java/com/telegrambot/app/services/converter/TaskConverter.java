package com.telegrambot.app.services.converter;

import com.telegrambot.app.DTO.api_1C.taskResponse.TaskResponse;
import com.telegrambot.app.DTO.api_1C.typesОbjects.Entity1C;
import com.telegrambot.app.model.Entity;
import com.telegrambot.app.model.documents.doc.service.Task;
import com.telegrambot.app.model.documents.docdata.PropertyData;
import com.telegrambot.app.model.documents.docdata.SyncData;
import com.telegrambot.app.model.reference.Manager;
import com.telegrambot.app.repositories.ManagerRepository;
import com.telegrambot.app.repositories.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TaskConverter extends Converter1C {

    private final ManagerRepository managerRepository;
    private final TaskRepository taskRepository;

    private final TaskTypeConverter typeConverter;
    private final TaskStatusConverter statusConverter;
    private final DataConverter dataConverter;

    @Override
    public <T extends Entity, R extends Entity1C> R convertToResponse(T entity) {
        if (entity instanceof Task task) {
            TaskResponse taskResponse = new TaskResponse();
            taskResponse.setGuid(convertToGuid(task));
//        taskResponse.setCode(task.getCode());
            taskResponse.setDate(convertToDate(task.getDate()));
            taskResponse.setComment(task.getComment());
            taskResponse.setGuidAuthor(task.getAuthor()); //TODO заменить потом на сущность
            taskResponse.setGuidManager(convertToGuid(task.getManager()));
            taskResponse.setDate(convertToDate(task.getDate()));
            taskResponse.setDescription(task.getDescription());
            taskResponse.setDecision(task.getDecision());
            taskResponse.setGuidStatus(convertToGuid(task.getStatus()));
            taskResponse.setClosingDate(convertToDate(task.getClosingDate()));
            taskResponse.setType(task.getType().getName());
            taskResponse.setGuidDivision(task.getDivision());
            taskResponse.setHighPriority(task.getProperties().getHighPriority());
            taskResponse.setIsOutsourcing(task.getProperties().getIsOutsourcing());
            taskResponse.setIsBilling(task.getProperties().getIsBilling());
            taskResponse.setGuidPartner(convertToGuid(task.getPartnerData().getPartner()));
            taskResponse.setGuidContract(convertToGuid(task.getPartnerData().getContract()));
            taskResponse.setGuidDepartment(convertToGuid(task.getPartnerData().getDepartment()));
            taskResponse.setTotalAmount(task.getTotalAmount().toString());
            return (R) taskResponse;
        }
        return null;
    }

    @Override
    public <T extends Entity, R extends Entity1C> T updateEntity(R dto, T entity) {
        if (dto instanceof TaskResponse response && entity instanceof Task entityBD) {
//            entityBD.setName(response.getName());
            entityBD.setSyncData(new SyncData(response.getGuid(), response.getCode()));
            entityBD.setDate(convertToLocalDateTime(response.getDate()));
            entityBD.setComment(response.getComment());
            entityBD.setAuthor(response.getGuidAuthor()); //TODO заменить потом на сущность
            entityBD.setManager(Converter1C.getOrCreateEntity(response.getGuidManager(), managerRepository, Manager.class));
            entityBD.setDate(convertToLocalDateTime(response.getDate()));
            entityBD.setDescription(response.getDescription());
            entityBD.setDecision(response.getDecision());
            entityBD.setDivision(response.getGuidDivision());
            entityBD.setStatus(statusConverter.getOrCreateEntity(response.getGuidStatus(), true));
            entityBD.setPartnerData(dataConverter.getPartnerData(response));
            entityBD.setClosingDate(convertToLocalDateTime(response.getClosingDate()));
            entityBD.setType(typeConverter.getOrCreateEntity(response.getType(), true));
            entityBD.setProperties(getProperties(response));
            entityBD.setTotalAmount(Integer.valueOf(response.getTotalAmount()));
            return entity;
        }
        return null;
    }

    private PropertyData getProperties(TaskResponse taskResponse) {
        PropertyData properties = new PropertyData();
        properties.setHighPriority(convertToBoolean(taskResponse.getHighPriority()));
        properties.setIsOutsourcing(convertToBoolean(taskResponse.getIsOutsourcing()));
        properties.setIsBilling(convertToBoolean(taskResponse.getIsBilling()));
        return properties;
    }

    @Override
    public <T extends Entity, R extends Entity1C> T getOrCreateEntity(R dto) {
        return (T) Converter1C.getOrCreateEntity(dto, taskRepository, Task.class);
    }

    @Override
    public <T extends Entity> T getOrCreateEntity(String guid, boolean isSaved) {
        return (T) Converter1C.getOrCreateEntity(guid, taskRepository, Task.class, isSaved);
    }
}
