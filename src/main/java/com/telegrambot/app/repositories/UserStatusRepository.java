package com.telegrambot.app.repositories;

import com.telegrambot.app.model.user.UserBD;
import com.telegrambot.app.model.user.UserStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserStatusRepository extends CrudRepository<UserStatus, Long> {
    @Transactional
    void deleteByUserBD(UserBD userBD);

    List<UserStatus> findByUserBDOrderByLastUpdateDesc(UserBD userBD);

    UserStatus findFirstByUserBDOrderByLastUpdateDesc(UserBD userBD);

    void deleteByIdAllIgnoreCase(Long id);

}
