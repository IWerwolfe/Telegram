package com.telegrambot.app.services.api;

import com.telegrambot.app.DTO.api.DataResponse;
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
import com.telegrambot.app.DTO.api.legal.contract.ContractDataListResponse;
import com.telegrambot.app.DTO.api.legal.contract.ContractDataResponse;
import com.telegrambot.app.DTO.api.legal.contract.ContractResponse;
import com.telegrambot.app.DTO.api.legal.department.DepartmentDataListResponse;
import com.telegrambot.app.DTO.api.legal.department.DepartmentDataResponse;
import com.telegrambot.app.DTO.api.legal.department.DepartmentResponse;
import com.telegrambot.app.DTO.api.legal.partner.PartnerDataResponse;
import com.telegrambot.app.DTO.api.legal.partner.PartnerResponse;
import com.telegrambot.app.DTO.api.manager.ManagerDataListResponse;
import com.telegrambot.app.DTO.api.manager.ManagerDataResponse;
import com.telegrambot.app.DTO.api.manager.ManagerResponse;
import com.telegrambot.app.DTO.api.taskStatus.TaskStatusDataListResponse;
import com.telegrambot.app.DTO.api.taskStatus.TaskStatusDataResponse;
import com.telegrambot.app.DTO.api.taskStatus.TaskStatusResponse;

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
