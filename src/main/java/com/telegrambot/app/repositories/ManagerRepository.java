package com.telegrambot.app.repositories;

import com.telegrambot.app.model.task.Manager;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ManagerRepository extends CrudRepository<Manager, Long> {
    Optional<Manager> findByGuid(String guid);
}