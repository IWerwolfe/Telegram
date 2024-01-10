package com.supportbot.repositories.doc;

import com.supportbot.model.documents.doc.service.TaskDoc;
import com.supportbot.model.reference.Manager;
import com.supportbot.model.reference.TaskStatus;
import com.supportbot.model.reference.legalentity.Department;
import com.supportbot.model.reference.legalentity.Partner;
import com.supportbot.model.user.UserBD;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskDocRepository extends EntityDocRepository<TaskDoc> {

    @Query("""
            select t from TaskDoc t
            where t.partnerData is not null and t.partnerData.department = ?1 and t.status <> ?2
            order by t.status DESC""")
    List<TaskDoc> findByPartnerDataNotNullAndPartnerData_DepartmentAndStatusOrderByStatusDesc(Department department, TaskStatus status);

    @Query("""
            select t from TaskDoc t
            where t.partnerData is not null and t.partnerData.partner = ?1 and t.status <> ?2
            order by t.date DESC""")
    List<TaskDoc> findByPartnerDataNotNullAndPartnerData_PartnerAndStatusOrderByDateDesc(Partner partner, TaskStatus status);

    @Query("select t from TaskDoc t where t.manager = ?1 and t.status <> ?2 order by t.date DESC")
    List<TaskDoc> findByManagerAndStatusOrderByDateDesc(Manager manager, TaskStatus status);

    @Query("select t from TaskDoc t where t.manager = ?1 and t.status <> ?2 order by t.date")
    List<TaskDoc> findByManagerAndStatusNotOrderByDateAsc(Manager manager, TaskStatus status);

    @Query("""
            select t from TaskDoc t
            where t.partnerData is not null and t.partnerData.department = ?1 and t.status <> ?2
            order by t.date""")
    List<TaskDoc> findByPartnerDataNotNullAndPartnerData_DepartmentAndStatusNotOrderByDateAsc(Department department, TaskStatus status);

    @Query("""
            select t from TaskDoc t
            where t.partnerData is not null and t.partnerData.partner = ?1 and t.status <> ?2
            order by t.date""")
    List<TaskDoc> findByPartnerDataNotNullAndPartnerData_PartnerAndStatusNotOrderByDateAsc(Partner partner, TaskStatus status);

    List<TaskDoc> findByCreatorAndStatusNotOrderByDateAsc(UserBD creator, TaskStatus status);

//    Optional<TaskDoc> findBySyncDataNotNullAndSyncData_Guid(String guid);
}