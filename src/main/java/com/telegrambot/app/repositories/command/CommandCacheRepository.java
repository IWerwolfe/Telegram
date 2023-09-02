package com.telegrambot.app.repositories.command;

import com.telegrambot.app.model.command.CommandCache;
import com.telegrambot.app.model.user.UserBD;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CommandCacheRepository extends CrudRepository<CommandCache, Long> {
    @Transactional
    void deleteByUserBD(UserBD userBD);

    long countByUserBD(UserBD userBD);

    List<CommandCache> findByUserBDOrderById(UserBD userBD);
}
