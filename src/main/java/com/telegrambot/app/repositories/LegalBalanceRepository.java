package com.telegrambot.app.repositories;

import com.telegrambot.app.model.balance.LegalBalance;
import com.telegrambot.app.model.legalentity.LegalEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface LegalBalanceRepository extends CrudRepository<LegalBalance, Long> {
    List<LegalBalance> findByLegalInOrderByLegal_NameAsc(Collection<LegalEntity> legals);

    Optional<LegalBalance> findByLegal(LegalEntity legal);
}