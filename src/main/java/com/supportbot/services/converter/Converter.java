package com.supportbot.services.converter;

import com.supportbot.DTO.api.typeObjects.EntityDocResponse;
import com.supportbot.DTO.api.typeObjects.EntityResponse;
import com.supportbot.model.documents.docdata.PartnerData;
import com.supportbot.model.types.Document;
import com.supportbot.model.types.Entity;
import com.supportbot.model.types.Reference;
import com.supportbot.repositories.EntityRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Slf4j
public abstract class Converter {

    private static final HashMap<String, String> entityForRequest = new HashMap<>();

    private static void addEntityForRequest(@NonNull String guid, String type) {
        entityForRequest.put(guid, type);
    }

    private static HashMap<String, String> getEntityForRequest() {
        return entityForRequest;
    }

//    private static get

    public abstract <T extends Entity, R extends EntityResponse> R convertToResponse(T entity);

    public abstract <T extends Entity, R extends EntityResponse> T updateEntity(R dto, T entity);

    public abstract <T extends Entity, R extends EntityResponse> T getOrCreateEntity(R dto);

    public abstract <T extends Entity> T getOrCreateEntity(String guid, boolean isSaved);

    public abstract <T extends Entity, S extends EntityResponse> List<T> convertToEntityList(List<S> list, boolean isSaved);

    public <E extends Document, T extends EntityDocResponse> T convertDocToResponse(E doc, Class<T> entityType) {
        T request = createEntity(entityType);
        if (request != null) {
            request.setDate(convertToDate(doc.getDate()));
            request.setComment(doc.getComment());
            request.setGuidCompany(convertToGuid(doc.getCompany()));
            request.setGuidDivision(convertToGuid(doc.getDivision()));
            request.setGuidManager(convertToGuid(doc.getManager()));
            request.setTotalAmount(String.valueOf(doc.getTotalAmount()));
            request.setGuid(convertToGuid(doc));
            request.setMarkedForDel(doc.getMarkedForDel());
            request.setGuidAuthor(doc.getAuthor());

            PartnerData partnerData = doc.getPartnerData();
            if (partnerData != null) {
                request.setGuidDepartment(convertToGuid(partnerData.getDepartment()));
                request.setGuidContract(convertToGuid(partnerData.getContract()));
                request.setGuidPartner(convertToGuid(partnerData.getPartner()));
            }
        }
        return request;
    }

    public <E extends Document, T extends EntityDocResponse> void fillResponseToDoc(E doc, T response) {
        doc.setDate(convertToLocalDateTime(response.getDate()));
        doc.setComment(response.getComment());
        doc.setTotalAmount(convertToInteger(response.getTotalAmount()));
        doc.setMarkedForDel(response.getMarkedForDel());
        doc.setAuthor(response.getGuidAuthor());
    }

    public <E extends Reference, T extends EntityResponse> T convertReferenceToResponse(E entity, Class<T> entityType) {

        T request = createEntity(entityType);
        if (request == null || entity == null) {
            return request;
        }

        request.setName(entity.getName());
        request.setGuid(entity.getGuidEntity());
        request.setMarkedForDel(entity.getMarkedForDel());

        return request;
    }

    public <T extends Entity, R extends EntityResponse> T convertToEntity(R dto) {

        if (dto == null) {
            return null;
        }

        T entity = getOrCreateEntity(dto);
        if (entity == null) {
            return null;
        }
        entity.setSyncData(dto.getGuid(), dto.getCode());
        return updateEntity(dto, entity);
    }

    public static <T extends Entity,
            S extends EntityResponse,
            C extends Converter> List<T> convertToEntityList(List<S> list,
                                                             C converter) {
        return list == null || list.isEmpty() ?
                new ArrayList<>() :
                list.stream().map(response -> (T) converter.convertToEntity(response)).toList();
    }

    public static <T extends Entity,
            S extends EntityResponse,
            R extends EntityRepository<T>,
            C extends Converter> List<T> convertToEntityListIsSave(List<S> list,
                                                                   C converter,
                                                                   R repository) {
        List<T> entityList = convertToEntityList(list, converter);
        return entityList == null || entityList.isEmpty() ?
                new ArrayList<>() :
                repository.saveAllAndFlush(entityList);
    }

    public static <T extends Entity, S extends EntityResponse, R extends EntityRepository<T>> T getOrCreateEntity(S dto,
                                                                                                                  R repository,
                                                                                                                  Class<T> entityType) {
        if (dto == null) {
            return null;
        }
        if ((dto.getGuid() == null || dto.getGuid().isEmpty()) && (dto.getCode() != null || dto.getName() != null)) {
            return createEntity(entityType);
        }
        return getOrCreateEntity(dto.getGuid(), repository, entityType, true);
    }

    public static <T extends Entity, R extends EntityRepository<T>> T getOrCreateEntity(String guid,
                                                                                        R repository,
                                                                                        Class<T> entityType) {
        return getOrCreateEntity(guid, repository, entityType, false);
    }

    public static <T extends Entity, R extends EntityRepository<T>> T getOrCreateEntity(String guid,
                                                                                        R repository,
                                                                                        Class<T> entityType,
                                                                                        boolean isSaved) {
        if (guid == null || guid.isEmpty()) {
            return null;
        }
        Optional<T> existingEntity = repository.findBySyncDataNotNullAndSyncData_Guid(guid);
        return existingEntity.orElseGet(() -> createEntity(guid, repository, entityType, isSaved));
    }

    public static <T extends Entity, R extends EntityRepository<T>> T createEntity(String guid,
                                                                                   R repository,
                                                                                   Class<T> entityType,
                                                                                   boolean isSaved) {
        T entity = createEntity(guid, entityType);
        return isSaved && entity != null ? repository.save(entity) : entity;
    }

    public static <T extends Entity> T createEntity(String guid, Class<T> entityType) {
        T entity = createEntity(entityType);
        if (entity != null) {
            entity.setSyncData(guid, "");
            addEntityForRequest(guid, entityType.getName());
        }
        return entity;
    }

    public static <T> T createEntity(Class<T> entityType) {
        try {
            return entityType.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException e) {
            log.error("Ошибка создания сущности {}: {}", entityType.getSimpleName(), e.getMessage());
        }
        return null;
    }

    public static Integer convertToInteger(String sum) {
        try {
            return sum == null || sum.isEmpty() ? 0 : Integer.parseInt(sum);
        } catch (Exception e) {
            return 0;
        }
    }


    public static LocalDateTime convertToLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static Date convertToDate(LocalDateTime date) {
        if (date == null) {
            return null;
        }
        return Date.from(date.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDateTime convertLongToLocalDateTime(long longDate) {
        return longDate == 0L ? null : LocalDateTime.ofInstant(Instant.ofEpochMilli(longDate), ZoneId.systemDefault());
    }

    public static String convertToGuid(Entity entity) {
        if (entity == null || entity.getSyncData() == null) {
            return "";
        }
        return entity.getSyncData().getGuid();
    }

    public static Boolean convertToBoolean(Boolean field) {
        return field != null && field;
    }

    public static <T extends Enum<T>> T convertToEnum(String value, Class<T> enumClass) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            return Enum.valueOf(enumClass, value.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.error("Invalid enum value: {}", value);
            return null;
        }
    }

    public static <T extends Enum<T>> String convertEnumToString(T enumEntity) {
        if (enumEntity == null) {
            return "";
        }
        try {
            return enumEntity.name();
        } catch (Exception e) {
            log.error("Invalid enum: {}", e.getMessage());
            return "";
        }
    }
}
