package com.telegrambot.app.repositories;

import com.telegrambot.app.model.task.TaskStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TaskStatusRepository extends CrudRepository<TaskStatus, Long> {
    Optional<TaskStatus> findByNameIgnoreCase(String name);

    Optional<TaskStatus> findByGuid(String guid);
}