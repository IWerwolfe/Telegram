package com.telegrambot.app.services.converter;

import com.telegrambot.app.model.Entity;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
public abstract class Request1CConverter {

//    protected <T> T getOrCreateEntity(String guid, JpaRepository<T, Long> repository, Supplier<T> entitySupplier) {
//        Optional<T> existingEntity = repository.findByGuid(guid);
//        return existingEntity.orElseGet(() -> entitySupplier.get());
//    }

    public <T, R> T convertToEntity(R dto) {
        T entity = getOrCreateEntity(dto);
        return updateEntity(dto, entity);
    }

    public abstract <T, R> T updateEntity(R dto, T entity);

    public abstract <T, R> T getOrCreateEntity(R dto);

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
