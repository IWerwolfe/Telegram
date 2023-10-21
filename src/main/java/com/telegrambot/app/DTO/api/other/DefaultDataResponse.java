package com.telegrambot.app.DTO.api.other;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api.reference.bank.BankResponse;
import com.telegrambot.app.DTO.api.reference.bankAccount.BankAccountResponse;
import com.telegrambot.app.DTO.api.reference.cashDesk.CashDeskResponse;
import com.telegrambot.app.DTO.api.reference.cashDeskKkm.CashDeskKkmResponse;
import com.telegrambot.app.DTO.api.reference.legal.company.CompanyResponse;
import com.telegrambot.app.DTO.api.reference.legal.division.DivisionResponse;
import com.telegrambot.app.DTO.api.reference.manager.ManagerResponse;
import com.telegrambot.app.DTO.api.reference.payTerminal.PayTerminalResponse;
import com.telegrambot.app.DTO.api.reference.taskStatus.TaskStatusResponse;
import com.telegrambot.app.DTO.api.reference.taskType.TaskTypeResponse;
import com.telegrambot.app.DTO.api.typeОbjects.DataResponse;
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
