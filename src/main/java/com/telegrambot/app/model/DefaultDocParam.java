package com.telegrambot.app.model;

import com.telegrambot.app.DTO.types.TaskType;
import com.telegrambot.app.model.reference.*;
import com.telegrambot.app.model.reference.legalentity.Company;
import com.telegrambot.app.model.reference.legalentity.Division;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "default_param")
public class DefaultDocParam {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guid_default_closed_status_id")
    private TaskStatus closedTaskDocStatus;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "default_initial_status_id")
    private TaskStatus initTaskDocStatus;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_doc_type_id")
    private TaskType taskDocType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private Manager manager;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_account_id")
    private BankAccount bankAccount;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cash_desk_id")
    private CashDesk cashDesk;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cash_desk_kkm_id")
    private CashDeskKkm cashDeskKkm;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_terminal_id")
    private PayTerminal payTerminal;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "division_id")
    private Division division;

}
