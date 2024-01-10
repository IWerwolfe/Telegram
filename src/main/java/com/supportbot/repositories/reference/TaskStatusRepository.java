package com.supportbot.repositories.reference;

import com.supportbot.model.reference.TaskStatus;
import com.supportbot.repositories.EntityRepository;

import java.util.Optional;

public interface TaskStatusRepository extends EntityRepository<TaskStatus> {
    Optional<TaskStatus> findByNameIgnoreCase(String name);

    Optional<TaskStatus> findBySyncDataNotNullAndSyncData_Guid(String guid);
}