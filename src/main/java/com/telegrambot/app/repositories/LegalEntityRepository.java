package com.telegrambot.app.repositories;

import com.telegrambot.app.model.legalentity.LegalEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LegalEntityRepository<T extends LegalEntity> extends EntityRepository<T> {
    Optional<T> findByInnIgnoreCaseAndKppIgnoreCase(String inn, String kpp);

    Optional<T> findBySyncDataNotNullAndSyncData_Guid(String guid);
}
