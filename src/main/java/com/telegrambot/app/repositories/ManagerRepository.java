package com.telegrambot.app.repositories;

import com.telegrambot.app.model.reference.Manager;

import java.util.Optional;

public interface ManagerRepository extends EntityRepository<Manager> {
    Optional<Manager> findBySyncDataNotNullAndSyncData_Guid(String guid);
}