package com.supportbot.DTO.api.other;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.supportbot.DTO.api.reference.bank.BankResponse;
import com.supportbot.DTO.api.reference.bankAccount.BankAccountResponse;
import com.supportbot.DTO.api.reference.cashDesk.CashDeskResponse;
import com.supportbot.DTO.api.reference.cashDeskKkm.CashDeskKkmResponse;
import com.supportbot.DTO.api.reference.legal.company.CompanyResponse;
import com.supportbot.DTO.api.reference.legal.division.DivisionResponse;
import com.supportbot.DTO.api.reference.manager.ManagerResponse;
import com.supportbot.DTO.api.reference.payTerminal.PayTerminalResponse;
import com.supportbot.DTO.api.reference.taskStatus.TaskStatusResponse;
import com.supportbot.DTO.api.reference.taskType.TaskTypeResponse;
import com.supportbot.DTO.api.typeObjects.DataResponse;
import lombok.Data;

import java.util.List;

@Data
//@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DefaultDataResponse extends DataResponse {
    private List<TaskStatusResponse> taskStatuses;
    private List<TaskTypeResponse> taskTypes;
    private List<ManagerResponse> managers;
    private List<CompanyResponse> companies;
    private List<DivisionResponse> divisions;
    private List<BankResponse> banks;
    private List<BankAccountResponse> bankAccounts;
    private List<PayTerminalResponse> payTerminals;
    private List<CashDeskKkmResponse> cashDeskKkms;
    private List<CashDeskResponse> cashDesks;


    private byte[] key;
    private String guidDefaultClosedStatus;
    private String guidDefaultInitialStatus;
    private String nameDefaultType;
    private String guidDefaultCompany;
    private String guidDefaultBankAccount;
    private String guidDefaultCashDesk;
    private String guidDefaultCashDeskKkm;
    private String guidDefaultPaymentTerminal;
    private String guidDefaultDivisions;
}
