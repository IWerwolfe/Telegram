package com.supportbot.repositories.reference;

import com.supportbot.model.reference.legalentity.Contract;
import com.supportbot.model.reference.legalentity.Partner;
import com.supportbot.repositories.EntityRepository;
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
