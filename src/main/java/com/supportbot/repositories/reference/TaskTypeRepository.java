package com.supportbot.repositories.reference;

import com.supportbot.DTO.types.TaskType;
import com.supportbot.repositories.EntityRepository;

import java.util.Optional;

public interface TaskTypeRepository extends EntityRepository<TaskType> {
    Optional<TaskType> findByNameIgnoreCase(String name);
}