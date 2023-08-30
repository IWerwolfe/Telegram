package com.telegrambot.app.repositories;

import com.telegrambot.app.model.balance.PartnerBalance;
import com.telegrambot.app.model.legalentity.Partner;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PartnerBalanceRepository extends CrudRepository<PartnerBalance, Long> {
    @Query("select p from PartnerBalance p where p.partner in ?1")
    List<PartnerBalance> findByPartnerIn(Collection<Partner> partners);

    @Query("select p from PartnerBalance p where p.partner in ?1 order by p.partner.name")
    List<PartnerBalance> findByPartnerInOrderByPartner_NameAsc(Collection<Partner> partners);

    @Query("select p from PartnerBalance p where p.partner = ?1")
    Optional<PartnerBalance> findByPartner(Partner partner);

}