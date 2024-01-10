package com.supportbot.repositories.doc;

import com.supportbot.model.reference.legalentity.Partner;
import com.supportbot.model.types.Document;
import com.supportbot.repositories.EntityRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Collection;
import java.util.List;

@NoRepositoryBean
public interface EntityDocRepository<T extends Document> extends EntityRepository<T> {

    List<T> findByPartnerDataNotNullAndPartnerData_Partner(Partner partner);

    List<T> findByPartnerDataNotNullAndPartnerData_PartnerIn(Collection<Partner> partners);


}
