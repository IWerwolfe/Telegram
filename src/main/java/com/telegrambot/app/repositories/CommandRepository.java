package com.telegrambot.app.repositories;

import com.telegrambot.app.model.command.Command;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommandRepository extends CrudRepository<Command, Long> {
}
