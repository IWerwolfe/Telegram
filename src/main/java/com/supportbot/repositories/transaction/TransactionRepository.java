package com.supportbot.repositories.transaction;

import com.supportbot.model.transaction.Transaction;
import org.springframework.data.repository.CrudRepository;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {
}
