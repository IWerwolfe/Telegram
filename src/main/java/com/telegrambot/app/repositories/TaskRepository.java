package com.telegrambot.app.repositories;

import com.telegrambot.app.model.task.Task;
import com.telegrambot.app.model.task.TaskStatus;
import com.telegrambot.app.model.user.UserBD;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends CrudRepository<Task, Long> {
    Optional<Task> findByCodeIgnoreCase(String code);

    List<Task> findByCreatorAndStatusNotOrderByDateAsc(UserBD creator, TaskStatus status);

    Optional<Task> findByGuid(String guid);
}