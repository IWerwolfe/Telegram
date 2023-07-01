package com.telegrambot.app.repositories;

import com.telegrambot.app.model.legalentity.Contract;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ContractRepository extends CrudRepository<Contract, Long> {
    Optional<Contract> findByGuid(String guid);
}
