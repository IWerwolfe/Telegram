package com.telegrambot.app.repositories;

import com.telegrambot.app.model.task.TaskType;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TaskTypeRepository extends CrudRepository<TaskType, Long> {
    Optional<TaskType> findByNameIgnoreCase(String name);
}