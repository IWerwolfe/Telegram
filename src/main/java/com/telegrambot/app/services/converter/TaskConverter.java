package com.telegrambot.app.services.converter;

import com.telegrambot.app.DTO.api_1C.TaskResponse;
import com.telegrambot.app.model.documents.doc.service.Task;
import com.telegrambot.app.model.documents.docdata.PartnerData;
import com.telegrambot.app.model.documents.docdata.SyncData;
import com.telegrambot.app.model.legalentity.Contract;
import com.telegrambot.app.model.legalentity.Department;
import com.telegrambot.app.model.legalentity.LegalEntity;
import com.telegrambot.app.model.legalentity.Partner;
import com.telegrambot.app.model.reference.Manager;
import com.telegrambot.app.model.reference.TaskStatus;
import com.telegrambot.app.model.task.Properties;
import com.telegrambot.app.model.task.TaskType;
import com.telegrambot.app.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class TaskConverter extends Request1CConverter {
    private final ManagerRepository managerRepository;
    private final TaskTypeRepository taskTypeRepository;
    private final DepartmentRepository departmentRepository;
    private final TaskStatusRepository taskStatusRepository;
    private final TaskRepository taskRepository;
    private final LegalEntityRepository partnerRepository;
    private final ContractRepository contractRepository;

    @Override
    public <T, R> T updateEntity(R dto, T entity) {
        if (dto instanceof TaskResponse response && entity instanceof Task entityBD) {
//            entityBD.setName(response.getName());
            entityBD.setSyncData(new SyncData(response.getGuid(), response.getCode()));
            entityBD.setDate(convertToLocalDateTime(response.getDate()));
            entityBD.setComment(response.getComment());
            entityBD.setAuthor(response.getGuidAuthor()); //TODO заменить потом на сущность
            entityBD.setManager(getOrCreateEntityManager(response.getGuidManager()));
            entityBD.setDate(convertToLocalDateTime(response.getDate()));
            entityBD.setDescription(response.getDescription());
            entityBD.setDecision(response.getDecision());
            entityBD.setStatus(getOrCreateEntityStatus(response.getGuidStatus()));
            entityBD.setPartnerData(getPartnerData(response));
            entityBD.setClosingDate(convertToLocalDateTime(response.getClosingDate()));
            entityBD.setType(getOrCreateEntityType(response.getType()));
            entityBD.setProperties(getProperties(response));

            return (T) entity;
        }
        return null;
    }

    @Override
    public <T, R> T getOrCreateEntity(R dto) {
        if (dto instanceof TaskResponse response) {
            if (response.getGuid() == null || response.getGuid().isEmpty()) {
                return null;
            }
            Optional<Task> existingEntity = taskRepository.findBySyncDataNotNullAndSyncData_Guid(response.getGuid());
            return (T) existingEntity.orElseGet(() -> new Task(response.getGuid()));
        }
        return null;
    }

    public TaskResponse convertTaskToTaskResponse(Task task) {
        TaskResponse taskResponse = new TaskResponse();
//        taskResponse.setName(task.getName());
        taskResponse.setGuid(convertToGuid(task));
//        taskResponse.setCode(task.getCode());
        taskResponse.setDate(convertToDate(task.getDate()));
        taskResponse.setComment(task.getComment());
        taskResponse.setGuidAuthor(task.getAuthor()); //TODO заменить потом на сущность
        taskResponse.setGuidManager(convertToGuid(task.getManager()));
        taskResponse.setDate(convertToDate(task.getDate()));
        taskResponse.setDescription(task.getDescription());
        taskResponse.setDecision(task.getDecision());
        taskResponse.setGuidStatus(convertToGuid(task.getStatus()));
        taskResponse.setClosingDate(convertToDate(task.getClosingDate()));
        taskResponse.setType(task.getType().getName());
        taskResponse.setHighPriority(task.getProperties().getHighPriority());
        taskResponse.setIsOutsourcing(task.getProperties().getIsOutsourcing());
        taskResponse.setIsBilling(task.getProperties().getIsBilling());
        taskResponse.setGuidPartner(convertToGuid(task.getPartnerData().getPartner()));
        taskResponse.setGuidContract(convertToGuid(task.getPartnerData().getContract()));
        taskResponse.setGuidDepartment(convertToGuid(task.getPartnerData().getDepartment()));

        return taskResponse;
    }

    private PartnerData getPartnerData(TaskResponse taskResponse) {
        PartnerData partnerData = new PartnerData();
        partnerData.setPartner(getOrCreateEntityPartner(taskResponse.getGuidPartner()));
        partnerData.setContract(getOrCreateEntityContract(taskResponse.getGuidContract()));
        partnerData.setDepartment(getOrCreateEntityDepartment(taskResponse.getGuidDepartment()));
        return partnerData;
    }

    private Properties getProperties(TaskResponse taskResponse) {
        Properties properties = new Properties();
        properties.setHighPriority(convertToBoolean(taskResponse.getHighPriority()));
        properties.setIsOutsourcing(convertToBoolean(taskResponse.getIsOutsourcing()));
        properties.setIsBilling(convertToBoolean(taskResponse.getIsBilling()));
        return properties;
    }

    protected Partner getOrCreateEntityPartner(String guid) {
        Optional<LegalEntity> existingEntity = partnerRepository.findBySyncDataNotNullAndSyncData_Guid(guid);
        return (Partner) existingEntity.orElseGet(() -> partnerRepository.save(new Partner(guid)));
    }

    protected Manager getOrCreateEntityManager(String guid) {
        Optional<Manager> existingEntity = managerRepository.findBySyncDataNotNullAndSyncData_Guid(guid);
        return existingEntity.orElseGet(() -> managerRepository.save(new Manager(guid)));
    }

    protected TaskType getOrCreateEntityType(String name) {
        Optional<TaskType> existingEntity = taskTypeRepository.findByNameIgnoreCase(name);
        return existingEntity.orElseGet(() -> taskTypeRepository.save(new TaskType(name)));
    }

    protected Department getOrCreateEntityDepartment(String guid) {
        Optional<Department> existingEntity = departmentRepository.findBySyncDataNotNullAndSyncData_Guid(guid);
        return existingEntity.orElseGet(() -> departmentRepository.save(new Department(guid)));
    }

    protected Contract getOrCreateEntityContract(String guid) {
        Optional<Contract> existingEntity = contractRepository.findBySyncDataNotNullAndSyncData_Guid(guid);
        return existingEntity.orElseGet(() -> contractRepository.save(new Contract(guid)));
    }

    protected TaskStatus getOrCreateEntityStatus(String guid) {
        Optional<TaskStatus> existingEntity = taskStatusRepository.findBySyncDataNotNullAndSyncData_Guid(guid);
        return existingEntity.orElseGet(() -> taskStatusRepository.save(new TaskStatus(guid)));
    }
}
