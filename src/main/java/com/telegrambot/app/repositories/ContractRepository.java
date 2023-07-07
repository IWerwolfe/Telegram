package com.telegrambot.app.repositories;

import com.telegrambot.app.model.legalentity.Contract;
import com.telegrambot.app.model.legalentity.Partner;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ContractRepository extends CrudRepository<Contract, Long> {
    List<Contract> findByPartnerOrderByCodeAsc(Partner partner);

    List<Contract> findByPartner_GuidOrderByCodeAsc(String guid);

    Optional<Contract> findByGuid(String guid);
}
