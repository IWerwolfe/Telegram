package com.telegrambot.app.repositories.user;

import com.telegrambot.app.model.user.UserBD;
import com.telegrambot.app.model.user.UserStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserStatusRepository extends CrudRepository<UserStatus, Long> {
    List<UserStatus> findByUserBDAndLegalNotNull(UserBD userBD);

    @Transactional
    void deleteByUserBD(UserBD userBD);

    List<UserStatus> findByUserBDOrderByLastUpdateDesc(UserBD userBD);

    UserStatus findFirstByUserBDOrderByLastUpdateDesc(UserBD userBD);

    void deleteByIdAllIgnoreCase(Long id);

}
