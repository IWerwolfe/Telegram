package com.telegrambot.app.repositories;

import com.telegrambot.app.model.documents.doc.service.Task;
import com.telegrambot.app.model.reference.TaskStatus;
import com.telegrambot.app.model.user.UserBD;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends CrudRepository<Task, Long> {
    Optional<Task> findBySyncDataNotNullAndSyncData_CodeOrId(String code, Long id);

    List<Task> findByCreatorAndStatusNotOrderByDateAsc(UserBD creator, TaskStatus status);

    Optional<Task> findBySyncDataNotNullAndSyncData_Guid(String guid);
}