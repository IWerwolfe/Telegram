package com.telegrambot.app.services.converter;

import com.telegrambot.app.DTO.api_1C.TaskTypeResponse;
import com.telegrambot.app.model.task.TaskType;
import com.telegrambot.app.repositories.TaskTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class TaskTypeConverter extends Request1CConverter {

    private final TaskTypeRepository typeRepository;

    @Override
    public <T, R> T updateEntity(R dto, T entity) {
        if (dto instanceof TaskTypeResponse response && entity instanceof TaskType entityBD) {
            entityBD.setName(response.getName());
            entityBD.setDisplay(response.getDisplay());
            return (T) entity;
        }
        return null;
    }

    @Override
    public <T, R> T getOrCreateEntity(R dto) {
        if (dto instanceof TaskTypeResponse response) {
            if (response.getName() == null || response.getName().isEmpty()) {
                return null;
            }
            Optional<TaskType> existingEntity = typeRepository.findByNameIgnoreCase(response.getName());
            return (T) existingEntity.orElseGet(() -> new TaskType("Обращение"));
        }
        return null;
    }
}
