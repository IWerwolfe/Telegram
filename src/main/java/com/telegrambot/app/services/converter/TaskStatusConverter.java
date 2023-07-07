package com.telegrambot.app.services.converter;

import com.telegrambot.app.DTO.api_1C.TaskStatusResponse;
import com.telegrambot.app.model.task.TaskStatus;
import com.telegrambot.app.repositories.TaskStatusRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class TaskStatusConverter extends Request1CConverter {
    private final TaskStatusRepository statusRepository;

    @Override
    public <T, R> T updateEntity(R dto, T entity) {
        if (dto instanceof TaskStatusResponse response && entity instanceof TaskStatus entityBD) {
            entityBD.setName(response.getName());
            entityBD.setGuid(response.getGuid());
            entityBD.setCode(response.getCode());
            return (T) entity;
        }
        return null;
    }

    @Override
    public <T, R> T getOrCreateEntity(R dto) {
        if (dto instanceof TaskStatusResponse response) {
            if (response.getGuid() == null || response.getGuid().isEmpty()) {
                return null;
            }
            Optional<TaskStatus> existingEntity = statusRepository.findByGuid(response.getGuid());
            return (T) existingEntity.orElseGet(() -> new TaskStatus(response.getGuid()));
        }
        return null;
    }
}
