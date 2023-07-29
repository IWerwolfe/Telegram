package com.telegrambot.app.services.converter;

import com.telegrambot.app.DTO.api_1C.typesОbjects.Entity1C;
import com.telegrambot.app.model.Entity;
import com.telegrambot.app.repositories.EntityRepository;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Slf4j
public abstract class Converter1C {

    public abstract <T extends Entity, R extends Entity1C> R convertToResponse(T entity);

    public abstract <T extends Entity, R extends Entity1C> T updateEntity(R dto, T entity);

    public abstract <T extends Entity, R extends Entity1C> T getOrCreateEntity(R dto);

    public abstract <T extends Entity> T getOrCreateEntity(String guid, boolean isSaved);

    public <T extends Entity, R extends Entity1C> T convertToEntity(R dto) {
        T entity = getOrCreateEntity(dto);
        return updateEntity(dto, entity);
    }

    public static <T extends Entity, S extends Entity1C, R extends EntityRepository<T>> T getOrCreateEntity(S dto,
                                                                                                            R repository,
                                                                                                            Class<T> entityType) {
        if (dto.getGuid() == null || dto.getGuid().isEmpty()) return null;
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

    private static <T extends Entity, R extends EntityRepository<T>> T createEntity(String guid,
                                                                                    R repository,
                                                                                    Class<T> entityType,
                                                                                    boolean isSaved) {
        try {
            T entity = entityType.getDeclaredConstructor(String.class).newInstance(guid);
            return isSaved ? repository.save(entity) : entity;

        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException e) {
            log.error("Ошибка создания сущности {}: {}", entityType.getSimpleName(), e.getMessage());
        }
        return null;
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
}
