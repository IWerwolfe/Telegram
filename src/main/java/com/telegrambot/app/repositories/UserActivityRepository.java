package com.telegrambot.app.repositories;

import com.telegrambot.app.model.User;
import com.telegrambot.app.model.UserActivity;
import org.springframework.data.repository.CrudRepository;

public interface UserActivityRepository extends CrudRepository<UserActivity, Long> {
    UserActivity findByUser(User user);
}
