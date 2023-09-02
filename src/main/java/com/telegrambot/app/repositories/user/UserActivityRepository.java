package com.telegrambot.app.repositories.user;

import com.telegrambot.app.model.user.UserActivity;
import com.telegrambot.app.model.user.UserBD;
import org.springframework.data.repository.CrudRepository;

public interface UserActivityRepository extends CrudRepository<UserActivity, Long> {
    UserActivity findByUserBD(UserBD userBD);
}
