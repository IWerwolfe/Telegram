package com.supportbot.repositories.user;

import com.supportbot.model.user.UserActivity;
import org.springframework.data.repository.CrudRepository;

public interface UserActivityRepository extends CrudRepository<UserActivity, Long> {

}
