package com.supportbot.repositories.command;

import com.supportbot.model.command.Command;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommandRepository extends CrudRepository<Command, Long> {
}
