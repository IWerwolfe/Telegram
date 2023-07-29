package com.telegrambot.app.repositories;

import com.telegrambot.app.model.legalentity.Department;

import java.util.Optional;

public interface DepartmentRepository extends EntityRepository<Department> {

    Optional<Department> findBySyncDataNotNullAndSyncData_Guid(String guid);
}
