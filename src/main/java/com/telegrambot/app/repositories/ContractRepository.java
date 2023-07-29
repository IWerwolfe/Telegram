package com.telegrambot.app.repositories;

import com.telegrambot.app.model.legalentity.Contract;
import com.telegrambot.app.model.legalentity.Partner;

import java.util.List;
import java.util.Optional;

public interface ContractRepository extends EntityRepository<Contract> {
    List<Contract> findByPartnerOrderByIdAsc(Partner partner);

    Optional<Contract> findBySyncDataNotNullAndSyncData_Guid(String guid);
}
