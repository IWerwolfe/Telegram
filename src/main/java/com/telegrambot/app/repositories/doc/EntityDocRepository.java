package com.telegrambot.app.repositories.doc;

import com.telegrambot.app.model.reference.legalentity.Partner;
import com.telegrambot.app.model.types.Document;
import com.telegrambot.app.repositories.EntityRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Collection;
import java.util.List;

@NoRepositoryBean
public interface EntityDocRepository<T extends Document> extends EntityRepository<T> {

    List<T> findByPartnerDataNotNullAndPartnerData_Partner(Partner partner);

    List<T> findByPartnerDataNotNullAndPartnerData_PartnerIn(Collection<Partner> partners);


}
