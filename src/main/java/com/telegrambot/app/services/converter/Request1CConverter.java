package com.telegrambot.app.services.converter;

import lombok.extern.slf4j.Slf4j;

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

    protected abstract <T, R> T getOrCreateEntity(R dto);

    protected LocalDateTime convertToLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    protected <T extends Enum<T>> T convertToEnum(String value, Class<T> enumClass) {
        try {
            return Enum.valueOf(enumClass, value.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            log.error("Invalid enum value: {}", value);
            return null;
        }
    }
}
