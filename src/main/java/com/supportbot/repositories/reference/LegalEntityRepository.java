package com.supportbot.repositories.reference;

import com.supportbot.model.reference.legalentity.LegalEntity;
import com.supportbot.repositories.EntityRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LegalEntityRepository<T extends LegalEntity> extends EntityRepository<T> {
    Optional<T> findByInnIgnoreCaseAndKppIgnoreCase(String inn, String kpp);

    Optional<T> findBySyncDataNotNullAndSyncData_Guid(String guid);
}
