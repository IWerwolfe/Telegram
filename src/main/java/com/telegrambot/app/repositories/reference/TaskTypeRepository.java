package com.telegrambot.app.repositories.reference;

import com.telegrambot.app.DTO.types.TaskType;
import com.telegrambot.app.repositories.EntityRepository;

import java.util.Optional;

public interface TaskTypeRepository extends EntityRepository<TaskType> {
    Optional<TaskType> findByNameIgnoreCase(String name);
}