package com.supportbot.repositories.reference;

import com.supportbot.model.reference.legalentity.Department;
import com.supportbot.model.reference.legalentity.Partner;
import com.supportbot.repositories.EntityRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends EntityRepository<Department> {
    @Query("select d from Department d where d.partner in ?1")
    List<Department> findByPartnerIn(Collection<Partner> partners);

    @Query("select d from Department d where d.partner = ?1")
    List<Department> findByPartner(Partner partner);

    @Query("""
            select d from Department d
            where d.partner is not null and d.partner.syncData is not null and d.syncData.guid = ?1""")
    List<Department> findByPartnerNotNullAndPartner_SyncDataNotNullAndSyncData_Guid(String guid);

    @Query("select d from Department d where d.partner.inn = ?1")
    List<Department> findByPartner_Inn(String inn);

    Optional<Department> findBySyncDataNotNullAndSyncData_Guid(String guid);
}
