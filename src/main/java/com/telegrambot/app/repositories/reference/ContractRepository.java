package com.telegrambot.app.repositories.reference;

import com.telegrambot.app.model.legalentity.Contract;
import com.telegrambot.app.model.legalentity.Partner;
import com.telegrambot.app.repositories.EntityRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ContractRepository extends EntityRepository<Contract> {
    @Query("select c from Contract c where c.partner in ?1")
    List<Contract> findByPartnerIn(Collection<Partner> partners);

    List<Contract> findByPartnerOrderByIdAsc(Partner partner);

    Optional<Contract> findBySyncDataNotNullAndSyncData_Guid(String guid);

    List<Contract> findByPartner_Inn(String inn);

    List<Contract> findByPartner(Partner partner);
}
