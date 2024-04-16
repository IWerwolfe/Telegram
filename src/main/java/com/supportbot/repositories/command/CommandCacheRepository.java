package com.supportbot.repositories.command;

import com.supportbot.model.command.CommandCache;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CommandCacheRepository extends CrudRepository<CommandCache, Long> {
    @Query("select c from CommandCache c where c.date < ?1")
    List<CommandCache> findByDateBefore(LocalDateTime date);
}
