package com.telegrambot.app.services.converter;

import com.telegrambot.app.DTO.api_1C.taskResponse.TaskResponse;
import com.telegrambot.app.DTO.api_1C.typeОbjects.Entity1C;
import com.telegrambot.app.model.Entity;
import com.telegrambot.app.model.documents.doc.payment.CardDoc;
import com.telegrambot.app.model.documents.doc.service.Task;
import com.telegrambot.app.repositories.CardDocRepository;
import com.telegrambot.app.repositories.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CardDocConverter extends Converter1C {
    private final CardDocRepository cardDocRepository;

    private final TaskRepository taskRepository;

    @Override
    public <T extends Entity, R extends Entity1C> R convertToResponse(T entity) {
        return null;
    }

    @Override
    public <T extends Entity, R extends Entity1C> T updateEntity(R dto, T entity) {
        if (dto instanceof TaskResponse response && entity instanceof Task entityBD) {
//            entityBD.setName(response.getName());
//            entityBD.setSyncData(new SyncData(response.getGuid(), response.getCode()));
//            entityBD.setDate(convertToLocalDateTime(response.getDate()));
//            entityBD.setComment(response.getComment());
//            entityBD.setAuthor(response.getGuidAuthor()); //TODO заменить потом на сущность
//            entityBD.setManager(getOrCreateEntityManager(response.getGuidManager()));
//            entityBD.setDate(convertToLocalDateTime(response.getDate()));
//            entityBD.setDescription(response.getDescription());
//            entityBD.setDecision(response.getDecision());
//            entityBD.setStatus(getOrCreateEntityStatus(response.getGuidStatus()));
//            entityBD.setPartnerData(getPartnerData(response));
//            entityBD.setClosingDate(convertToLocalDateTime(response.getClosingDate()));
//            entityBD.setType(getOrCreateEntityType(response.getType()));
//            entityBD.setProperties(getProperties(response));

            return (T) entity;
        }
        return null;
    }

    @Override
    public <T extends Entity, R extends Entity1C> T getOrCreateEntity(R dto) {
        return (T) Converter1C.getOrCreateEntity(dto, cardDocRepository, CardDoc.class);
    }

    @Override
    public <T extends Entity> T getOrCreateEntity(String guid, boolean isSaved) {
        return (T) Converter1C.getOrCreateEntity(guid, cardDocRepository, CardDoc.class, isSaved);
    }
}
