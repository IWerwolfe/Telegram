package com.telegrambot.app.repositories;

import com.telegrambot.app.model.documents.doc.service.Task;
import com.telegrambot.app.model.legalentity.Department;
import com.telegrambot.app.model.legalentity.Partner;
import com.telegrambot.app.model.reference.Manager;
import com.telegrambot.app.model.reference.TaskStatus;
import com.telegrambot.app.model.user.UserBD;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends EntityRepository<Task> {
    @Query("select t from Task t where t.manager = ?1 and t.status <> ?2 order by t.date")
    List<Task> findByManagerAndStatusNotOrderByDateAsc(Manager manager, TaskStatus status);

    @Query("""
            select t from Task t
            where t.partnerData is not null and t.partnerData.department = ?1 and t.status <> ?2
            order by t.date""")
    List<Task> findByPartnerDataNotNullAndPartnerData_DepartmentAndStatusNotOrderByDateAsc(Department department, TaskStatus status);

    @Query("""
            select t from Task t
            where t.partnerData is not null and t.partnerData.partner = ?1 and t.status <> ?2
            order by t.date""")
    List<Task> findByPartnerDataNotNullAndPartnerData_PartnerAndStatusNotOrderByDateAsc(Partner partner, TaskStatus status);

    List<Task> findByCreatorAndStatusNotOrderByDateAsc(UserBD creator, TaskStatus status);

//    Optional<Task> findBySyncDataNotNullAndSyncData_Guid(String guid);
}