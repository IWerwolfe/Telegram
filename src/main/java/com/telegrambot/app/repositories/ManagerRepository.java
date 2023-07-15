package com.telegrambot.app.repositories;

import com.telegrambot.app.model.reference.Manager;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ManagerRepository extends CrudRepository<Manager, Long> {
    Optional<Manager> findBySyncDataNotNullAndSyncData_Guid(String guid);
}