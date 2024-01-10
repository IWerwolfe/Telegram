package com.supportbot.repositories.reference;

import com.supportbot.model.reference.Manager;
import com.supportbot.repositories.EntityRepository;

import java.util.Optional;

public interface ManagerRepository extends EntityRepository<Manager> {
    Optional<Manager> findBySyncDataNotNullAndSyncData_Guid(String guid);
}