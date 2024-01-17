package com.supportbot.services.api;

import com.supportbot.DTO.api.balance.BalanceDataListResponse;
import com.supportbot.DTO.api.balance.BalanceResponse;
import com.supportbot.DTO.api.doc.bankDoc.BankDocDataListResponse;
import com.supportbot.DTO.api.doc.bankDoc.BankDocDataResponse;
import com.supportbot.DTO.api.doc.bankDoc.BankDocResponse;
import com.supportbot.DTO.api.doc.cardDoc.CardDocDataListResponse;
import com.supportbot.DTO.api.doc.cardDoc.CardDocDataResponse;
import com.supportbot.DTO.api.doc.cardDoc.CardDocResponse;
import com.supportbot.DTO.api.doc.cashDoc.CashDocDataListResponse;
import com.supportbot.DTO.api.doc.cashDoc.CashDocDataResponse;
import com.supportbot.DTO.api.doc.cashDoc.CashDocResponse;
import com.supportbot.DTO.api.doc.taskDoc.TaskDocDataListResponse;
import com.supportbot.DTO.api.doc.taskDoc.TaskDocDataResponse;
import com.supportbot.DTO.api.doc.taskDoc.TaskDocResponse;
import com.supportbot.DTO.api.reference.legal.contract.ContractDataListResponse;
import com.supportbot.DTO.api.reference.legal.contract.ContractDataResponse;
import com.supportbot.DTO.api.reference.legal.contract.ContractResponse;
import com.supportbot.DTO.api.reference.legal.department.DepartmentDataListResponse;
import com.supportbot.DTO.api.reference.legal.department.DepartmentDataResponse;
import com.supportbot.DTO.api.reference.legal.department.DepartmentResponse;
import com.supportbot.DTO.api.reference.legal.partner.PartnerDataResponse;
import com.supportbot.DTO.api.reference.legal.partner.PartnerResponse;
import com.supportbot.DTO.api.reference.manager.ManagerDataListResponse;
import com.supportbot.DTO.api.reference.manager.ManagerDataResponse;
import com.supportbot.DTO.api.reference.manager.ManagerResponse;
import com.supportbot.DTO.api.reference.taskStatus.TaskStatusDataListResponse;
import com.supportbot.DTO.api.reference.taskStatus.TaskStatusDataResponse;
import com.supportbot.DTO.api.reference.taskStatus.TaskStatusResponse;
import com.supportbot.DTO.api.reference.taskType.TaskTypeDataListResponse;
import com.supportbot.DTO.api.reference.taskType.TaskTypeDataResponse;
import com.supportbot.DTO.api.reference.taskType.TaskTypeResponse;
import com.supportbot.DTO.api.typeObjects.DataResponse;

public interface ApiInService {

    TaskDocDataResponse getTask(String guid);

    TaskDocDataListResponse getTasks(String guidManager, String guidCompany, String guidDepartment);

    TaskDocDataListResponse getNotSyncTasks();

    DataResponse createTask(TaskDocResponse response);

    DataResponse updateTask(TaskDocResponse response);

    DataResponse delTask(String guid);


    PartnerDataResponse getPartner(String guid);

    PartnerDataResponse getPartners(String inn);

    PartnerDataResponse getNotSyncPartners();

    DataResponse createPartner(PartnerDataResponse response);

    DataResponse updatePartner(PartnerResponse response);

    DataResponse delPartner(String guid);


    DepartmentDataResponse getDepartment(String guid);

    DepartmentDataListResponse getDepartments(String inn, String guidPartner);

    DepartmentDataListResponse getNotSyncDepartments();

    DataResponse createDepartment(DepartmentResponse response);

    DataResponse updateDepartment(DepartmentResponse response);

    DataResponse delDepartment(String guid);


    ContractDataResponse getContract(String guid);

    ContractDataListResponse getContracts(String inn, String guidPartner);

    ContractDataListResponse getNotSyncContracts();

    DataResponse createContract(ContractResponse response);

    DataResponse updateContract(ContractResponse response);

    DataResponse delContract(String guid);


    ManagerDataResponse getManager(String guid);

    ManagerDataListResponse getManagers();

    ManagerDataListResponse getNotSyncManagers();

    DataResponse createManager(ManagerResponse response);

    DataResponse updateManager(ManagerResponse response);

    DataResponse delManager(String guid);


    TaskTypeDataResponse getTaskType(String guid);

    TaskTypeDataListResponse getTaskTypes();

    TaskTypeDataListResponse getNotSyncTaskTypes();

    DataResponse createTaskType(TaskTypeResponse response);

    DataResponse updateTaskType(TaskTypeResponse response);

    DataResponse delTaskType(String guid);


    TaskStatusDataResponse getTaskStatus(String guid);

    TaskStatusDataListResponse getTaskStatuses();

    TaskStatusDataListResponse getNotSyncTaskStatuses();

    DataResponse createTaskStatus(TaskStatusResponse response);

    DataResponse updateTaskStatus(TaskStatusResponse response);

    DataResponse delTaskStatus(String guid);


    BalanceDataListResponse getBalances(String inn, String guidPartner);

    DataResponse updateBalance(BalanceResponse response);


    BankDocDataResponse getBankDoc(String guid);

    BankDocDataListResponse getBankDocs(String inn, String guidPartner);

    BankDocDataListResponse getNotSyncBankDocs();

    DataResponse createBankDoc(BankDocResponse response);

    DataResponse updateBankDoc(BankDocResponse response);

    DataResponse delBankDoc(String guid);


    CardDocDataResponse getCardDoc(String guid);

    CardDocDataListResponse getCardDocs(String inn, String guidPartner);

    CardDocDataListResponse getNotSyncCardDocs();

    DataResponse createCardDoc(CardDocResponse response);

    DataResponse updateCardDoc(CardDocResponse response);

    DataResponse delCardDoc(String guid);


    CashDocDataResponse getCashDoc(String guid);

    CashDocDataListResponse getCashDocs(String inn, String guidPartner);

    CashDocDataListResponse getNotSyncCashDocs();

    DataResponse createCashDoc(CashDocResponse response);

    DataResponse updateCashDoc(CashDocResponse response);

    DataResponse delCashDoc(String guid);


}
