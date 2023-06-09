package com.telegrambot.app.repositories;

import com.telegrambot.app.model.UserStatus;
import org.springframework.data.repository.CrudRepository;

public interface UserStatusRepository extends CrudRepository<UserStatus, Long> {
}
