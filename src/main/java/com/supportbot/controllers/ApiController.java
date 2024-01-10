package com.supportbot.controllers;

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
import com.supportbot.DTO.api.typeОbjects.DataResponse;
import com.supportbot.services.api.ApiInService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor
public class ApiController {

    private final ApiInService api;

    @GetMapping("/task")
    public ResponseEntity<TaskDocDataResponse> getTask(@RequestParam("guid") String guid) {
        return ResponseEntity.ok(api.getTask(guid));
    }

    @GetMapping("/tasks")
    public ResponseEntity<TaskDocDataListResponse> getTasks(@RequestParam(defaultValue = "") String guidManager,
                                                            @RequestParam(defaultValue = "") String guidPartner,
                                                            @RequestParam(defaultValue = "") String guidDepartment,
                                                            @RequestParam(defaultValue = "false") boolean nonSync) {
        TaskDocDataListResponse response = nonSync ?
                api.getNotSyncTasks() : api.getTasks(guidManager, guidPartner, guidDepartment);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/task")
    public ResponseEntity<DataResponse> createTask(@RequestBody TaskDocResponse response) {
        return ResponseEntity.ok(api.createTask(response));
    }

    @PutMapping("/task")
    public ResponseEntity<DataResponse> updateTask(@RequestBody TaskDocResponse response) {
        return ResponseEntity.ok(api.updateTask(response));
    }

    @DeleteMapping("/task")
    public ResponseEntity<DataResponse> delTask(@RequestParam("guid") String guid) {
        return ResponseEntity.ok(api.delTask(guid));
    }

    @GetMapping("/partner")
    public ResponseEntity<PartnerDataResponse> getPartner(@RequestParam("guid") String guid) {
        return ResponseEntity.ok(api.getPartner(guid));
    }

    @GetMapping("/partners")
    public ResponseEntity<PartnerDataResponse> getPartners(@RequestParam(defaultValue = "") String inn,
                                                           @RequestParam(defaultValue = "false") boolean nonSync) {
        PartnerDataResponse response = nonSync ?
                api.getNotSyncPartners() : api.getPartners(inn);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/partner")
    public ResponseEntity<DataResponse> createPartner(@RequestBody PartnerDataResponse response) {
        return ResponseEntity.ok(api.createPartner(response));
    }

    @PutMapping("/partner")
    public ResponseEntity<DataResponse> updatePartner(@RequestBody PartnerResponse response) {
        return ResponseEntity.ok(api.updatePartner(response));
    }

    @DeleteMapping("/partner")
    public ResponseEntity<DataResponse> delPartner(@RequestParam("guid") String guid) {
        return ResponseEntity.ok(api.delPartner(guid));
    }

    @GetMapping("/department")
    public ResponseEntity<DepartmentDataResponse> getDepartment(@RequestParam("guid") String guid) {
        return ResponseEntity.ok(api.getDepartment(guid));
    }

    @GetMapping("/departments")
    public ResponseEntity<DepartmentDataListResponse> getDepartments(@RequestParam(defaultValue = "") String inn,
                                                                     @RequestParam(defaultValue = "") String guidPartner,
                                                                     @RequestParam(defaultValue = "false") boolean nonSync) {
        DepartmentDataListResponse response = nonSync ?
                api.getNotSyncDepartments() : api.getDepartments(inn, guidPartner);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/department")
    public ResponseEntity<DataResponse> createDepartment(@RequestBody DepartmentResponse response) {
        return ResponseEntity.ok(api.createDepartment(response));
    }

    @PutMapping("/department")
    public ResponseEntity<DataResponse> updateDepartment(@RequestBody DepartmentResponse response) {
        return ResponseEntity.ok(api.updateDepartment(response));
    }

    @DeleteMapping("/department")
    public ResponseEntity<DataResponse> delDepartment(@RequestParam("guid") String guid) {
        return ResponseEntity.ok(api.delDepartment(guid));
    }

    @GetMapping("/contract")
    public ResponseEntity<ContractDataResponse> getContract(@RequestParam("guid") String guid) {
        return ResponseEntity.ok(api.getContract(guid));
    }

    @GetMapping("/contracts")
    public ResponseEntity<ContractDataListResponse> getContracts(@RequestParam(defaultValue = "") String inn,
                                                                 @RequestParam(defaultValue = "") String guidPartner,
                                                                 @RequestParam(defaultValue = "false") boolean nonSync) {
        ContractDataListResponse response = nonSync ?
                api.getNotSyncContracts() : api.getContracts(inn, guidPartner);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/contract")
    public ResponseEntity<DataResponse> createContract(@RequestBody ContractResponse response) {
        return ResponseEntity.ok(api.createContract(response));
    }

    @PutMapping("/contract")
    public ResponseEntity<DataResponse> updateContract(@RequestBody ContractResponse response) {
        return ResponseEntity.ok(api.updateContract(response));
    }

    @DeleteMapping("/contract")
    public ResponseEntity<DataResponse> delContract(@RequestParam("guid") String guid) {
        return ResponseEntity.ok(api.delContract(guid));
    }

    @GetMapping("/manager")
    public ResponseEntity<ManagerDataResponse> getManager(@RequestParam("guid") String guid) {
        return ResponseEntity.ok(api.getManager(guid));
    }

    @GetMapping("/managers")
    public ResponseEntity<ManagerDataListResponse> getManagers(@RequestParam(defaultValue = "false") boolean nonSync) {
        ManagerDataListResponse response = nonSync ?
                api.getNotSyncManagers() : api.getManagers();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/manager")
    public ResponseEntity<DataResponse> createManager(@RequestBody ManagerResponse response) {
        return ResponseEntity.ok(api.createManager(response));
    }

    @PutMapping("/manager")
    public ResponseEntity<DataResponse> updateManager(@RequestBody ManagerResponse response) {
        return ResponseEntity.ok(api.updateManager(response));
    }

    @DeleteMapping("/manager")
    public ResponseEntity<DataResponse> delManager(@RequestParam("guid") String guid) {
        return ResponseEntity.ok(api.delManager(guid));
    }

    @GetMapping("/taskType")
    public ResponseEntity<TaskTypeDataResponse> getTaskType(@RequestParam("guid") String guid) {
        return ResponseEntity.ok(api.getTaskType(guid));
    }

    @GetMapping("/taskTypes")
    public ResponseEntity<TaskTypeDataListResponse> getTaskTypes(@RequestParam(defaultValue = "false") boolean nonSync) {
        TaskTypeDataListResponse response = nonSync ?
                api.getNotSyncTaskTypes() : api.getTaskTypes();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/taskType")
    public ResponseEntity<DataResponse> createTaskType(@RequestBody TaskTypeResponse response) {
        return ResponseEntity.ok(api.createTaskType(response));
    }

    @PutMapping("/taskType")
    public ResponseEntity<DataResponse> updateTaskType(@RequestBody TaskTypeResponse response) {
        return ResponseEntity.ok(api.updateTaskType(response));
    }

    @DeleteMapping("/taskType")
    public ResponseEntity<DataResponse> delTaskType(@RequestParam("guid") String guid) {
        return ResponseEntity.ok(api.delTaskType(guid));
    }

    @GetMapping("/taskStatus")
    public ResponseEntity<TaskStatusDataResponse> getTaskStatus(@RequestParam("guid") String guid) {
        return ResponseEntity.ok(api.getTaskStatus(guid));
    }

    @GetMapping("/taskStatuses")
    public ResponseEntity<TaskStatusDataListResponse> getTaskStatuses(@RequestParam(defaultValue = "false") boolean nonSync) {
        TaskStatusDataListResponse response = nonSync ?
                api.getNotSyncTaskStatuses() : api.getTaskStatuses();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/taskStatus")
    public ResponseEntity<DataResponse> createTaskStatus(@RequestBody TaskStatusResponse response) {
        return ResponseEntity.ok(api.createTaskStatus(response));
    }

    @PutMapping("/taskStatus")
    public ResponseEntity<DataResponse> updateTaskStatus(@RequestBody TaskStatusResponse response) {
        return ResponseEntity.ok(api.updateTaskStatus(response));
    }

    @DeleteMapping("/taskStatus")
    public ResponseEntity<DataResponse> delTaskStatus(@RequestParam("guid") String guid) {
        return ResponseEntity.ok(api.delTaskStatus(guid));
    }

    @GetMapping("/balance")
    public ResponseEntity<BalanceDataListResponse> getBalances(@RequestParam(defaultValue = "") String inn,
                                                               @RequestParam(defaultValue = "") String guidPartner) {
        return ResponseEntity.ok(api.getBalances(inn, guidPartner));
    }

    @PutMapping("/balance")
    public ResponseEntity<DataResponse> updateBalance(@RequestBody BalanceResponse response) {
        return ResponseEntity.ok(api.updateBalance(response));
    }

    @GetMapping("/bankDoc")
    public ResponseEntity<BankDocDataResponse> getBankDoc(@RequestParam("guid") String guid) {
        return ResponseEntity.ok(api.getBankDoc(guid));
    }

    @GetMapping("/bankDocs")
    public ResponseEntity<BankDocDataListResponse> getBankDocs(@RequestParam(defaultValue = "") String inn,
                                                               @RequestParam(defaultValue = "") String guidPartner,
                                                               @RequestParam(defaultValue = "false") boolean nonSync) {
        BankDocDataListResponse response = nonSync ?
                api.getNotSyncBankDocs() : api.getBankDocs(inn, guidPartner);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/bankDoc")
    public ResponseEntity<DataResponse> createBankDoc(@RequestBody BankDocResponse response) {
        return ResponseEntity.ok(api.createBankDoc(response));
    }

    @PutMapping("/bankDoc")
    public ResponseEntity<DataResponse> updateBankDoc(@RequestBody BankDocResponse response) {
        return ResponseEntity.ok(api.updateBankDoc(response));
    }

    @DeleteMapping("/bankDoc")
    public ResponseEntity<DataResponse> delBankDoc(@RequestParam("guid") String guid) {
        return ResponseEntity.ok(api.delBankDoc(guid));
    }

    @GetMapping("/cardDoc")
    public ResponseEntity<CardDocDataResponse> getCardDoc(@RequestParam("guid") String guid) {
        return ResponseEntity.ok(api.getCardDoc(guid));
    }

    @GetMapping("/cardDocs")
    public ResponseEntity<CardDocDataListResponse> getCardDocs(@RequestParam(defaultValue = "") String inn,
                                                               @RequestParam(defaultValue = "") String guidPartner,
                                                               @RequestParam(defaultValue = "false") boolean nonSync) {
        CardDocDataListResponse response = nonSync ?
                api.getNotSyncCardDocs() : api.getCardDocs(inn, guidPartner);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/cardDoc")
    public ResponseEntity<DataResponse> createCardDoc(@RequestBody CardDocResponse response) {
        return ResponseEntity.ok(api.createCardDoc(response));
    }

    @PutMapping("/cardDoc")
    public ResponseEntity<DataResponse> updateCardDoc(@RequestBody CardDocResponse response) {
        return ResponseEntity.ok(api.updateCardDoc(response));
    }

    @DeleteMapping("/cardDoc")
    public ResponseEntity<DataResponse> delCardDoc(@RequestParam("guid") String guid) {
        return ResponseEntity.ok(api.delCardDoc(guid));
    }

    @GetMapping("/cashDoc")
    public ResponseEntity<CashDocDataResponse> getCashDoc(@RequestParam("guid") String guid) {
        return ResponseEntity.ok(api.getCashDoc(guid));
    }

    @GetMapping("/cashDocs")
    public ResponseEntity<CashDocDataListResponse> getCashDocs(@RequestParam(defaultValue = "") String inn,
                                                               @RequestParam(defaultValue = "") String guidPartner,
                                                               @RequestParam(defaultValue = "false") boolean nonSync) {
        CashDocDataListResponse response = nonSync ?
                api.getNotSyncCashDocs() : api.getCashDocs(inn, guidPartner);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/cashDoc")
    public ResponseEntity<DataResponse> createCashDoc(@RequestBody CashDocResponse response) {
        return ResponseEntity.ok(api.createCashDoc(response));
    }

    @PutMapping("/cashDoc")
    public ResponseEntity<DataResponse> updateCashDoc(@RequestBody CashDocResponse response) {
        return ResponseEntity.ok(api.updateCashDoc(response));
    }

    @DeleteMapping("/cashDoc")
    public ResponseEntity<DataResponse> delCashDoc(@RequestParam("guid") String guid) {
        return ResponseEntity.ok(api.delCashDoc(guid));
    }
}
