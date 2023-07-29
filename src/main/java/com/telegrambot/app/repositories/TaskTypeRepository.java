package com.telegrambot.app.repositories;

import com.telegrambot.app.DTO.types.TaskType;

import java.util.Optional;

public interface TaskTypeRepository extends EntityRepository<TaskType> {
    Optional<TaskType> findByNameIgnoreCase(String name);
}