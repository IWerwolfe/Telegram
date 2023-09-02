package com.telegrambot.app.repositories.reference;

import com.telegrambot.app.model.reference.legalentity.LegalEntity;
import com.telegrambot.app.repositories.EntityRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LegalEntityRepository<T extends LegalEntity> extends EntityRepository<T> {
    Optional<T> findByInnIgnoreCaseAndKppIgnoreCase(String inn, String kpp);

    Optional<T> findBySyncDataNotNullAndSyncData_Guid(String guid);
}
