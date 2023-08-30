package com.telegrambot.app.repositories.reference;

import com.telegrambot.app.model.reference.Manager;
import com.telegrambot.app.repositories.EntityRepository;

import java.util.Optional;

public interface ManagerRepository extends EntityRepository<Manager> {
    Optional<Manager> findBySyncDataNotNullAndSyncData_Guid(String guid);
}