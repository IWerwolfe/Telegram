package com.telegrambot.app.repositories;

import com.telegrambot.app.model.legalentity.LegalEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LegalEntityRepository extends EntityRepository<LegalEntity> {
    Optional<LegalEntity> findByInnIgnoreCaseAndKppIgnoreCase(String inn, String kpp);

    Optional<LegalEntity> findBySyncDataNotNullAndSyncData_Guid(String guid);
}
