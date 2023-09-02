package com.telegrambot.app.repositories.transaction;

import com.telegrambot.app.model.transaction.Transaction;
import org.springframework.data.repository.CrudRepository;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {
}
