package com.telegrambot.app.repositories.reference;

import com.telegrambot.app.model.reference.TaskStatus;
import com.telegrambot.app.repositories.EntityRepository;

import java.util.Optional;

public interface TaskStatusRepository extends EntityRepository<TaskStatus> {
    Optional<TaskStatus> findByNameIgnoreCase(String name);

    Optional<TaskStatus> findBySyncDataNotNullAndSyncData_Guid(String guid);
}