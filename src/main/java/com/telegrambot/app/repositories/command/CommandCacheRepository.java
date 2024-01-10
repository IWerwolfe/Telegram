package com.telegrambot.app.repositories.command;

import com.telegrambot.app.model.command.CommandCache;
import com.telegrambot.app.model.user.UserBD;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommandCacheRepository extends CrudRepository<CommandCache, Long> {

    long countByUserBD(UserBD userBD);

    List<CommandCache> findByUserBDOrderById(UserBD userBD);
}
