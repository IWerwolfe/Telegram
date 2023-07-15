package com.telegrambot.app.repositories;

import com.telegrambot.app.model.legalentity.Department;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface DepartmentRepository extends CrudRepository<Department, Long> {

    Optional<Department> findBySyncDataNotNullAndSyncData_Guid(String guid);
}
