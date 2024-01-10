package com.supportbot.repositories.command;

import com.supportbot.model.command.CommandCache;
import com.supportbot.model.user.UserBD;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommandCacheRepository extends CrudRepository<CommandCache, Long> {

    long countByUserBD(UserBD userBD);

    List<CommandCache> findByUserBDOrderById(UserBD userBD);
}
