package com.telegrambot.app.repositories.transaction;

import com.telegrambot.app.model.transaction.FinTransaction;
import org.springframework.data.repository.CrudRepository;

public interface FinTransactionRepository extends CrudRepository<FinTransaction, Long> {
}