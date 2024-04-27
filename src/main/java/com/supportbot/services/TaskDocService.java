package com.supportbot.services;

import com.supportbot.DTO.api.doc.taskDoc.TaskDocDataListResponse;
import com.supportbot.DTO.api.typeObjects.DataResponse;
import com.supportbot.client.ApiClient;
import com.supportbot.model.documents.doc.service.TaskDoc;
import com.supportbot.model.reference.Manager;
import com.supportbot.model.reference.TaskStatus;
import com.supportbot.model.reference.legalentity.Department;
import com.supportbot.model.reference.legalentity.LegalEntity;
import com.supportbot.model.reference.legalentity.Partner;
import com.supportbot.model.user.UserBD;
import com.supportbot.repositories.doc.TaskDocRepository;
import com.supportbot.services.converter.Converter;
import com.supportbot.services.converter.TaskDocConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskDocService {

    private final TaskDocRepository taskDocRepository;
    private final TaskDocConverter taskDocConverter;
    private final ApiClient api1C;

    public List<TaskDoc> getTaskListByApiByManager(Manager manager, UserBD user) {
        TaskDocDataListResponse response = api1C.getTaskListDataByManager(user.getGuidEntity());
        return isCompleted(response) ?
                taskDocConverter.convertToEntityList(response.getList(), true) :
                taskDocRepository.findByManagerAndStatusNotOrderByDateAsc(manager, TaskStatus.getClosedStatus());
    }

    public List<TaskDoc> getTaskListByApiByCompany(LegalEntity legal) {
        TaskDocDataListResponse response = api1C.getTaskListDataByCompany(Converter.convertToGuid(legal));
        return isCompleted(response) ?
                taskDocConverter.convertToEntityList(response.getList(), true) :
                taskDocRepository.findByPartnerDataNotNullAndPartnerData_PartnerAndStatusNotOrderByDateAsc((Partner) legal,
                        TaskStatus.getClosedStatus());
    }

    public List<TaskDoc> getTaskListByApiByDepartment(Department department) {
        TaskDocDataListResponse response = api1C.getTaskListDataByDepartment(Converter.convertToGuid(department));
        return isCompleted(response) ?
                taskDocConverter.convertToEntityList(response.getList(), true) :
                taskDocRepository.findByPartnerDataNotNullAndPartnerData_DepartmentAndStatusNotOrderByDateAsc(department,
                        TaskStatus.getClosedStatus());
    }

    public List<TaskDoc> getTaskListByApiByUser(UserBD user) {
        if (user.getGuidEntity() == null) {
            return taskDocRepository.
                    findByCreatorAndStatusNotOrderByDateAsc(user, TaskStatus.getClosedStatus());
        }
        TaskDocDataListResponse response = api1C.getTaskListDataByUser(user.getGuidEntity());
        return isCompleted(response) ?
                Converter.convertToEntityListIsSave(response.getList(), taskDocConverter, taskDocRepository) :
                taskDocRepository.
                        findByCreatorAndStatusNotOrderByDateAsc(user, TaskStatus.getClosedStatus());
    }

    private static <R extends DataResponse> boolean isCompleted(R response) {
        return response != null && response.isResult();
    }
}
