package com.supportbot.repositories.transaction;

import com.supportbot.model.transaction.FinTransaction;
import org.springframework.data.repository.CrudRepository;

public interface FinTransactionRepository extends CrudRepository<FinTransaction, Long> {
}