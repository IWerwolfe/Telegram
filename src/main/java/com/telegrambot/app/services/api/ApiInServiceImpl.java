package com.telegrambot.app.services.api;

import com.telegrambot.app.DTO.api.balance.BalanceDataListResponse;
import com.telegrambot.app.DTO.api.balance.BalanceResponse;
import com.telegrambot.app.DTO.api.doc.bankDoc.BankDocDataListResponse;
import com.telegrambot.app.DTO.api.doc.bankDoc.BankDocDataResponse;
import com.telegrambot.app.DTO.api.doc.bankDoc.BankDocResponse;
import com.telegrambot.app.DTO.api.doc.cardDoc.CardDocDataListResponse;
import com.telegrambot.app.DTO.api.doc.cardDoc.CardDocDataResponse;
import com.telegrambot.app.DTO.api.doc.cardDoc.CardDocResponse;
import com.telegrambot.app.DTO.api.doc.cashDoc.CashDocDataListResponse;
import com.telegrambot.app.DTO.api.doc.cashDoc.CashDocDataResponse;
import com.telegrambot.app.DTO.api.doc.cashDoc.CashDocResponse;
import com.telegrambot.app.DTO.api.doc.taskDoc.TaskDocDataListResponse;
import com.telegrambot.app.DTO.api.doc.taskDoc.TaskDocDataResponse;
import com.telegrambot.app.DTO.api.doc.taskDoc.TaskDocResponse;
import com.telegrambot.app.DTO.api.reference.legal.contract.ContractDataListResponse;
import com.telegrambot.app.DTO.api.reference.legal.contract.ContractDataResponse;
import com.telegrambot.app.DTO.api.reference.legal.contract.ContractResponse;
import com.telegrambot.app.DTO.api.reference.legal.department.DepartmentDataListResponse;
import com.telegrambot.app.DTO.api.reference.legal.department.DepartmentDataResponse;
import com.telegrambot.app.DTO.api.reference.legal.department.DepartmentResponse;
import com.telegrambot.app.DTO.api.reference.legal.partner.PartnerDataResponse;
import com.telegrambot.app.DTO.api.reference.legal.partner.PartnerResponse;
import com.telegrambot.app.DTO.api.reference.manager.ManagerDataListResponse;
import com.telegrambot.app.DTO.api.reference.manager.ManagerDataResponse;
import com.telegrambot.app.DTO.api.reference.manager.ManagerResponse;
import com.telegrambot.app.DTO.api.reference.taskStatus.TaskStatusDataListResponse;
import com.telegrambot.app.DTO.api.reference.taskStatus.TaskStatusDataResponse;
import com.telegrambot.app.DTO.api.reference.taskStatus.TaskStatusResponse;
import com.telegrambot.app.DTO.api.reference.taskType.TaskTypeDataListResponse;
import com.telegrambot.app.DTO.api.reference.taskType.TaskTypeDataResponse;
import com.telegrambot.app.DTO.api.reference.taskType.TaskTypeResponse;
import com.telegrambot.app.DTO.api.typeОbjects.DataEntityResponse;
import com.telegrambot.app.DTO.api.typeОbjects.DataListResponse;
import com.telegrambot.app.DTO.api.typeОbjects.DataResponse;
import com.telegrambot.app.DTO.api.typeОbjects.EntityResponse;
import com.telegrambot.app.DTO.types.EventSource;
import com.telegrambot.app.DTO.types.OperationType;
import com.telegrambot.app.DTO.types.TaskType;
import com.telegrambot.app.model.EntitySavedEvent;
import com.telegrambot.app.model.balance.PartnerBalance;
import com.telegrambot.app.model.documents.doc.payment.BankDoc;
import com.telegrambot.app.model.documents.doc.payment.CardDoc;
import com.telegrambot.app.model.documents.doc.payment.CashDoc;
import com.telegrambot.app.model.documents.doc.service.TaskDoc;
import com.telegrambot.app.model.reference.Manager;
import com.telegrambot.app.model.reference.TaskStatus;
import com.telegrambot.app.model.reference.legalentity.Contract;
import com.telegrambot.app.model.reference.legalentity.Department;
import com.telegrambot.app.model.reference.legalentity.Partner;
import com.telegrambot.app.model.types.Entity;
import com.telegrambot.app.repositories.EntityRepository;
import com.telegrambot.app.repositories.balance.PartnerBalanceRepository;
import com.telegrambot.app.repositories.doc.BankDocRepository;
import com.telegrambot.app.repositories.doc.CardDocRepository;
import com.telegrambot.app.repositories.doc.CashDocRepository;
import com.telegrambot.app.repositories.doc.TaskDocRepository;
import com.telegrambot.app.repositories.reference.*;
import com.telegrambot.app.services.converter.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApiInServiceImpl implements ApiInService {
    private final PartnerBalanceRepository partnerBalanceRepository;
    private final CashDocRepository cashDocRepository;
    private final CardDocRepository cardDocRepository;
    private final BankDocRepository bankDocRepository;
    private final TaskStatusRepository taskStatusRepository;
    private final ContractRepository contractRepository;
    private final DepartmentRepository departmentRepository;
    private final PartnerRepository partnerRepository;
    private final ManagerRepository managerRepository;
    private final TaskDocRepository taskDocRepository;
    private final TaskTypeRepository taskTypeRepository;

    private final PartnerConverter partnerConverter;
    private final TaskDocConverter taskDocConverter;
    private final DepartmentConverter departmentConverter;
    private final ContractConverter contractConverter;
    private final TaskStatusConverter taskStatusConverter;
    private final ManagerConverter managerConverter;
    private final CardDocConverter cardDocConverter;
    private final BankDocConverter bankDocConverter;
    private final CashDocConverter cashDocConverter;
    private final BalanceConverter balanceConverter;
    private final TaskTypeConverter taskTypeConverter;

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public TaskDocDataResponse getTask(String guid) {
        return getDataResponse(guid, taskDocConverter, taskDocRepository, TaskDocDataResponse.class);
    }

    @Override
    public TaskDocDataListResponse getTasks(String guidManager, String guidPartner, String guidDepartment) {
        List<TaskDoc> list = getTaskList(guidManager, guidPartner, guidDepartment);
        return getDataListResponse(list, taskDocConverter, TaskDocDataListResponse.class);
    }

    @Override
    public TaskDocDataListResponse getNotSyncTasks() {
        List<TaskDoc> list = taskDocRepository.findBySyncDataNull();
        return getDataListResponse(list, taskDocConverter, TaskDocDataListResponse.class);
    }

    @Override
    public DataResponse createTask(TaskDocResponse response) {
        return createEntity(response, taskDocConverter, taskDocRepository);
    }

    @Override
    public DataResponse updateTask(TaskDocResponse response) {
        return updateEntity(response, taskDocConverter, taskDocRepository);
    }

    @Override
    public DataResponse delTask(String guid) {
        return delEntity(guid, taskDocRepository);
    }

    @Override
    public PartnerDataResponse getPartner(String guid) {
        List<Partner> partners = getPartnerByBD(null, guid);
        return getPartnerDataResponse(partners);
    }

    @Override
    public PartnerDataResponse getPartners(String inn) {
        List<Partner> partners = getPartnerByBD(inn, null);
        return getPartnerDataResponse(partners);
    }

    @Override
    public PartnerDataResponse getNotSyncPartners() {
        List<Partner> partners = partnerRepository.findBySyncDataNull();
        return getPartnerDataResponse(partners);
    }

    @Override
    public DataResponse createPartner(PartnerDataResponse response) {
        if (response.getPartners() == null || response.getPartners().isEmpty()) {
            return new DataResponse(false, "The request does not contain the necessary data");
        }
        response.getPartners().forEach(r -> createEntity(r, partnerConverter, partnerRepository));
        response.getContracts().forEach(r -> createEntity(r, contractConverter, contractRepository));
        response.getDepartments().forEach(r -> createEntity(r, departmentConverter, departmentRepository));
        response.getBalance().forEach(this::updateOrCreateBalance);
        return new DataResponse(true, "");
    }

    @Override
    public DataResponse updatePartner(PartnerResponse response) {
        return updateEntity(response, partnerConverter, partnerRepository);
    }

    @Override
    public DataResponse delPartner(String guid) {
        List<Partner> partners = getPartnerByBD(null, guid);
        if (partners.isEmpty()) {
            return new DataResponse(false, "Data not found");
        }

        List<Contract> contracts = contractRepository.findByPartnerIn(partners);
        List<Department> departments = departmentRepository.findByPartnerIn(partners);

        delEntities(partners, partnerRepository);
        delEntities(departments, departmentRepository);
        delEntities(contracts, contractRepository);
        return new DataResponse(true, "");
    }

    @Override
    public DepartmentDataResponse getDepartment(String guid) {
        return getDataResponse(guid, departmentConverter, departmentRepository, DepartmentDataResponse.class);
    }

    @Override
    public DepartmentDataListResponse getDepartments(String inn, String guidPartner) {
        List<Partner> partners = getPartnerByBD(inn, guidPartner);
        return getDataListResponse(
                departmentRepository.findByPartnerIn(partners),
                departmentConverter,
                DepartmentDataListResponse.class);
    }

    @Override
    public DepartmentDataListResponse getNotSyncDepartments() {
        List<Department> list = departmentRepository.findBySyncDataNull();
        return getDataListResponse(list, departmentConverter, DepartmentDataListResponse.class);
    }

    @Override
    public DataResponse createDepartment(DepartmentResponse response) {
        return createEntity(response, departmentConverter, departmentRepository);
    }

    @Override
    public DataResponse updateDepartment(DepartmentResponse response) {
        return updateEntity(response, departmentConverter, departmentRepository);
    }

    @Override
    public DataResponse delDepartment(String guid) {
        return delEntity(guid, departmentRepository);
    }

    @Override
    public ContractDataResponse getContract(String guid) {
        return getDataResponse(guid, contractConverter, contractRepository, ContractDataResponse.class);
    }

    @Override
    public ContractDataListResponse getContracts(String inn, String guidPartner) {
        List<Partner> partners = getPartnerByBD(inn, guidPartner);
        return getDataListResponse(
                contractRepository.findByPartnerIn(partners),
                contractConverter,
                ContractDataListResponse.class);
    }

    @Override
    public ContractDataListResponse getNotSyncContracts() {
        List<Contract> list = contractRepository.findBySyncDataNull();
        return getDataListResponse(list, contractConverter, ContractDataListResponse.class);
    }

    @Override
    public DataResponse createContract(ContractResponse response) {
        return createEntity(response, contractConverter, contractRepository);
    }

    @Override
    public DataResponse updateContract(ContractResponse response) {
        return updateEntity(response, contractConverter, contractRepository);
    }

    @Override
    public DataResponse delContract(String guid) {
        return delEntity(guid, contractRepository);
    }

    @Override
    public ManagerDataResponse getManager(String guid) {
        return getDataResponse(guid, managerConverter, managerRepository, ManagerDataResponse.class);
    }

    @Override
    public ManagerDataListResponse getManagers() {
        return getDataListResponse(
                managerRepository.findAll(),
                managerConverter,
                ManagerDataListResponse.class);
    }

    @Override
    public ManagerDataListResponse getNotSyncManagers() {
        List<Manager> list = managerRepository.findBySyncDataNull();
        return getDataListResponse(list, managerConverter, ManagerDataListResponse.class);
    }

    @Override
    public DataResponse createManager(ManagerResponse response) {
        return createEntity(response, managerConverter, managerRepository);
    }

    @Override
    public DataResponse updateManager(ManagerResponse response) {
        return updateEntity(response, managerConverter, managerRepository);
    }

    @Override
    public DataResponse delManager(String guid) {
        return delEntity(guid, managerRepository);
    }

    @Override
    public TaskTypeDataResponse getTaskType(String guid) {
        return getDataResponse(guid, taskTypeConverter, taskTypeRepository, TaskTypeDataResponse.class);
    }

    @Override
    public TaskTypeDataListResponse getTaskTypes() {
        return getDataListResponse(
                taskTypeRepository.findAll(),
                taskTypeConverter,
                TaskTypeDataListResponse.class);
    }

    @Override
    public TaskTypeDataListResponse getNotSyncTaskTypes() {
        List<TaskType> list = taskTypeRepository.findBySyncDataNull();
        return getDataListResponse(list, taskTypeConverter, TaskTypeDataListResponse.class);
    }

    @Override
    public DataResponse createTaskType(TaskTypeResponse response) {
        return createEntity(response, taskTypeConverter, taskTypeRepository);
    }

    @Override
    public DataResponse updateTaskType(TaskTypeResponse response) {
        return updateEntity(response, taskTypeConverter, taskTypeRepository);
    }

    @Override
    public DataResponse delTaskType(String guid) {
        return delEntity(guid, taskTypeRepository);
    }

    @Override
    public TaskStatusDataResponse getTaskStatus(String guid) {
        return getDataResponse(guid, taskStatusConverter, taskStatusRepository, TaskStatusDataResponse.class);
    }

    @Override
    public TaskStatusDataListResponse getTaskStatuses() {
        return getDataListResponse(
                taskStatusRepository.findAll(),
                taskStatusConverter,
                TaskStatusDataListResponse.class);
    }

    @Override
    public TaskStatusDataListResponse getNotSyncTaskStatuses() {
        List<TaskStatus> list = taskStatusRepository.findBySyncDataNull();
        return getDataListResponse(list, taskStatusConverter, TaskStatusDataListResponse.class);
    }

    @Override
    public DataResponse createTaskStatus(TaskStatusResponse response) {
        return createEntity(response, taskStatusConverter, taskStatusRepository);
    }

    @Override
    public DataResponse updateTaskStatus(TaskStatusResponse response) {
        return updateEntity(response, taskStatusConverter, taskStatusRepository);
    }

    @Override
    public DataResponse delTaskStatus(String guid) {
        return delEntity(guid, taskStatusRepository);
    }

    @Override
    public BalanceDataListResponse getBalances(String inn, String guidPartner) {
        List<Partner> list = getPartnerByBD(inn, guidPartner);
        List<PartnerBalance> balances = partnerBalanceRepository.findByPartnerInOrderByPartner_NameAsc(list);
        List<BalanceResponse> responses = new ArrayList<>();

        balances.forEach(b -> responses.add(balanceConverter.convertToResponse(b)));
        BalanceDataListResponse dataResponse = new BalanceDataListResponse();
        dataResponse.setResult(!responses.isEmpty());
        dataResponse.setList(responses);
        return dataResponse;
    }

    @Override
    public DataResponse updateBalance(BalanceResponse response) {
        updateOrCreateBalance(response);
        return new DataResponse(true, "");
    }

    private void updateOrCreateBalance(BalanceResponse response) {
        PartnerBalance balance = balanceConverter.getOrCreateEntity(response);
        balanceConverter.updateEntity(response, balance);
        partnerBalanceRepository.save(balance);
    }

    @Override
    public BankDocDataResponse getBankDoc(String guid) {
        return getDataResponse(guid, bankDocConverter, bankDocRepository, BankDocDataResponse.class);
    }

    @Override
    public BankDocDataListResponse getBankDocs(String inn, String guidPartner) {
        List<Partner> partners = getPartnerByBD(inn, guidPartner);
        return getDataListResponse(
                bankDocRepository.findByPartnerDataNotNullAndPartnerData_PartnerIn(partners),
                bankDocConverter,
                BankDocDataListResponse.class);
    }

    @Override
    public BankDocDataListResponse getNotSyncBankDocs() {
        List<BankDoc> list = bankDocRepository.findBySyncDataNull();
        return getDataListResponse(list, bankDocConverter, BankDocDataListResponse.class);
    }

    @Override
    public DataResponse createBankDoc(BankDocResponse response) {
        return createEntity(response, bankDocConverter, bankDocRepository);
    }

    @Override
    public DataResponse updateBankDoc(BankDocResponse response) {
        return updateEntity(response, bankDocConverter, bankDocRepository);
    }

    @Override
    public DataResponse delBankDoc(String guid) {
        return delEntity(guid, bankDocRepository);
    }

    @Override
    public CardDocDataResponse getCardDoc(String guid) {
        return getDataResponse(guid, cardDocConverter, cardDocRepository, CardDocDataResponse.class);
    }

    @Override
    public CardDocDataListResponse getCardDocs(String inn, String guidPartner) {
        List<Partner> partners = getPartnerByBD(inn, guidPartner);
        return getDataListResponse(
                cardDocRepository.findByPartnerDataNotNullAndPartnerData_PartnerIn(partners),
                cardDocConverter,
                CardDocDataListResponse.class);
    }

    @Override
    public CardDocDataListResponse getNotSyncCardDocs() {
        List<CardDoc> list = cardDocRepository.findBySyncDataNull();
        return getDataListResponse(list, cardDocConverter, CardDocDataListResponse.class);
    }

    @Override
    public DataResponse createCardDoc(CardDocResponse response) {
        return createEntity(response, cardDocConverter, cardDocRepository);
    }

    @Override
    public DataResponse updateCardDoc(CardDocResponse response) {
        return updateEntity(response, cardDocConverter, cardDocRepository);
    }

    @Override
    public DataResponse delCardDoc(String guid) {
        return delEntity(guid, cardDocRepository);
    }

    @Override
    public CashDocDataResponse getCashDoc(String guid) {
        return getDataResponse(guid, cashDocConverter, cashDocRepository, CashDocDataResponse.class);
    }

    @Override
    public CashDocDataListResponse getCashDocs(String inn, String guidPartner) {
        List<Partner> partners = getPartnerByBD(inn, guidPartner);
        return getDataListResponse(
                cashDocRepository.findByPartnerDataNotNullAndPartnerData_PartnerIn(partners),
                cashDocConverter,
                CashDocDataListResponse.class);
    }

    @Override
    public CashDocDataListResponse getNotSyncCashDocs() {
        List<CashDoc> list = cashDocRepository.findBySyncDataNull();
        return getDataListResponse(list, cashDocConverter, CashDocDataListResponse.class);
    }

    @Override
    public DataResponse createCashDoc(CashDocResponse response) {
        return createEntity(response, cashDocConverter, cashDocRepository);
    }

    @Override
    public DataResponse updateCashDoc(CashDocResponse response) {
        return updateEntity(response, cashDocConverter, cashDocRepository);
    }

    @Override
    public DataResponse delCashDoc(String guid) {
        return delEntity(guid, cashDocRepository);
    }

    private PartnerDataResponse getPartnerDataResponse(List<Partner> partners) {

        PartnerDataResponse response = new PartnerDataResponse();

        if (partners.isEmpty()) {
            fillDataResponse(response, partners);
            return response;
        }

        List<Contract> contracts = contractRepository.findByPartnerIn(partners);
        List<Department> departments = departmentRepository.findByPartnerIn(partners);
        List<PartnerBalance> balances = partnerBalanceRepository.findByPartnerIn(partners);

        List<BalanceResponse> balanceResponses = new ArrayList<>();
        balances.forEach(b -> balanceResponses.add(balanceConverter.convertToResponse(b)));

        response.setPartners(getResponses(partners, partnerConverter));
        response.setContracts(getResponses(contracts, contractConverter));
        response.setDepartments(getResponses(departments, departmentConverter));
        response.setBalance(balanceResponses);

        return response;
    }

    private List<TaskDoc> getTaskList(String guidManager, String guidPartner, String guidDepartment) {
        if (guidIsCorrect(guidManager)) {
            return getTasksByGuidManager(guidManager);
        }
        if (guidIsCorrect(guidPartner)) {
            return getTasksByGuidCompany(guidPartner);
        }
        if (guidIsCorrect(guidDepartment)) {
            return getTasksByGuidDepartment(guidDepartment);
        }
        return new ArrayList<>();
    }

    private boolean guidIsCorrect(String guid) {
        return guid != null && !guid.isEmpty();
    }

    private List<TaskDoc> getTasksByGuidDepartment(String guid) {
        Optional<Department> optional = departmentRepository.findBySyncDataNotNullAndSyncData_Guid(guid);
        if (optional.isPresent()) {
            return taskDocRepository
                    .findByPartnerDataNotNullAndPartnerData_DepartmentAndStatusOrderByStatusDesc(optional.get(),
                            TaskStatus.getClosedStatus());
        }
        return new ArrayList<>();
    }

    private List<TaskDoc> getTasksByGuidCompany(String guid) {
        Optional<Partner> optional = partnerRepository.findBySyncDataNotNullAndSyncData_Guid(guid);
        if (optional.isPresent()) {
            return taskDocRepository
                    .findByPartnerDataNotNullAndPartnerData_PartnerAndStatusOrderByDateDesc(optional.get(),
                            TaskStatus.getClosedStatus());
        }
        return new ArrayList<>();
    }

    private List<TaskDoc> getTasksByGuidManager(String guid) {
        Optional<Manager> optional = managerRepository.findBySyncDataNotNullAndSyncData_Guid(guid);
        if (optional.isPresent()) {
            return taskDocRepository
                    .findByManagerAndStatusOrderByDateDesc(optional.get(),
                            TaskStatus.getClosedStatus());
        }
        return new ArrayList<>();
    }

    private List<Partner> getPartnerByBD(String inn, String guidPartner) {
        List<Partner> partners = new ArrayList<>();
        if (guidIsCorrect(inn)) {
            return partnerRepository.findByInnIgnoreCase(inn);
        }
        if (guidIsCorrect(guidPartner)) {
            Optional<Partner> optional = partnerRepository.findBySyncDataNotNullAndSyncData_Guid(guidPartner);
            optional.ifPresent(partners::add);
        }
        return partners;
    }

    private <T extends EntityResponse,
            C extends Converter,
            R extends EntityRepository<E>,
            E extends Entity> DataResponse createEntity(T response, C converter, R repository, OperationType operator) {

        try {
            E entity = converter.convertToEntity(response);
            repository.save(entity);
            eventPublisher.publishEvent(new EntitySavedEvent(entity, operator, EventSource.API));
            return new DataResponse(true, "");
        } catch (Exception e) {
            log.error("error when {} a entity: {}{}", operator.getLabel(), System.lineSeparator(), e.getMessage());
            return new DataResponse(false, e.getMessage());
        }
    }

    private <T extends EntityResponse,
            C extends Converter,
            R extends EntityRepository<E>,
            E extends Entity> DataResponse createEntity(T response, C converter, R repository) {

        return createEntity(response, converter, repository, OperationType.CREATE);
    }

    private <T extends EntityResponse,
            C extends Converter,
            R extends EntityRepository<E>,
            E extends Entity> DataResponse updateEntity(T response, C converter, R repository) {

        Optional<E> optional = repository.findBySyncDataNotNullAndSyncData_Guid(response.getGuid());
        OperationType operator = optional.isEmpty() ? OperationType.CREATE : OperationType.UPDATE;
        return createEntity(response, converter, repository, operator);
    }

    private <T extends EntityResponse,
            C extends Converter,
            E extends Entity> List<T> getResponses(List<E> list, C converter) {

        List<T> responses = new ArrayList<>();
        list.forEach(t -> responses.add(converter.convertToResponse(t)));
        return responses;
    }

    private <T extends EntityResponse,
            C extends Converter,
            R extends EntityRepository<E>,
            E extends Entity> T getResponse(String guid, C converter, R repository) {

        Optional<E> optional = repository.findBySyncDataNotNullAndSyncData_Guid(guid);
        return optional.<T>map(converter::convertToResponse).orElse(null);
    }

    private <T extends EntityResponse, D extends DataResponse> void fillDataResponse(D dataResponse, T response) {
        boolean result = (response != null);
        fillDataResponse(dataResponse, result);
    }

    private <T, D extends DataResponse> void fillDataResponse(D dataResponse, List<T> response) {
        boolean result = !(response == null || response.isEmpty());
        fillDataResponse(dataResponse, result);
    }

    private <D extends DataResponse> void fillDataResponse(D dataResponse, boolean result) {
        dataResponse.setResult(result);
        dataResponse.setError(result ? "" : "Data not found");
    }

    private <D extends DataEntityResponse<T>,
            T extends EntityResponse,
            E extends Entity,
            C extends Converter,
            R extends EntityRepository<E>> D getDataResponse(String guid, C converter, R repository, Class<D> classType) {

        T response = getResponse(guid, converter, repository);
        D dataResponse = createDataResponse(classType);
        fillDataResponse(dataResponse, response);
        dataResponse.setEntity(response);
        return dataResponse;
    }

    private <D extends DataListResponse<T>,
            T extends EntityResponse,
            E extends Entity,
            C extends Converter> D getDataListResponse(List<E> list, C converter, Class<D> classType) {

        List<T> responses = getResponses(list, converter);
        D dataResponse = createDataResponse(classType);
        fillDataResponse(dataResponse, responses);
        dataResponse.setList(responses);
        return dataResponse;
    }

    private <D extends DataResponse> D createDataResponse(Class<D> classType) {
        try {
            return classType.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            log.error("Error processing query results: {}{}", System.lineSeparator(), e.getMessage());
            return (D) new DataResponse(false, "Error processing query results");
        }
    }

    private <R extends EntityRepository<E>,
            E extends Entity> DataResponse delEntity(String guid, R repository) {

        Optional<E> optional = repository.findBySyncDataNotNullAndSyncData_Guid(guid);

        if (optional.isEmpty()) {
            return new DataResponse(false, "Data not found");
        }

        E entity = optional.get();
        entity.setMarkedForDel(true);
        repository.save(entity);

        return new DataResponse(true, "");
    }

    private <R extends EntityRepository<E>,
            E extends Entity> void delEntities(List<E> entities, R repository) {

        entities.forEach(entity -> {
            entity.setMarkedForDel(true);
            repository.save(entity);
        });
    }
}
