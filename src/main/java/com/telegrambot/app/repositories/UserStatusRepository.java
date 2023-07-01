package com.telegrambot.app.repositories;

import com.telegrambot.app.model.user.UserBD;
import com.telegrambot.app.model.user.UserStatus;
import org.springframework.data.repository.CrudRepository;

public interface UserStatusRepository extends CrudRepository<UserStatus, Long> {
    UserStatus findFirstByUserBDOrderByLastUpdateDesc(UserBD userBD);

    void deleteByIdAllIgnoreCase(Long id);

}
