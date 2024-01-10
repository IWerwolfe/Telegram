package com.supportbot.repositories.reference;

import com.supportbot.model.reference.legalentity.Partner;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartnerRepository extends LegalEntityRepository<Partner> {
    @Query("select p from Partner p where p.inn = ?1 and p.kpp = ?2")
    List<Partner> findByInnAndKpp(String inn, String kpp);

    @Query("select p from Partner p where upper(p.inn) = upper(?1)")
    List<Partner> findByInnIgnoreCase(String inn);
}
