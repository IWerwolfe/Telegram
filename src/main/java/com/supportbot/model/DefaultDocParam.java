package com.supportbot.model;

import com.supportbot.DTO.types.TaskType;
import com.supportbot.model.reference.*;
import com.supportbot.model.reference.legalentity.Company;
import com.supportbot.model.reference.legalentity.Division;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "default_param")
public class DefaultDocParam {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id")
    private Company company;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "guid_default_closed_status_id")
    private TaskStatus closedTaskDocStatus;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "default_initial_status_id")
    private TaskStatus initTaskDocStatus;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "task_doc_type_id")
    private TaskType taskDocType;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manager_id")
    private Manager manager;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bank_account_id")
    private BankAccount bankAccount;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cash_desk_id")
    private CashDesk cashDesk;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cash_desk_kkm_id")
    private CashDeskKkm cashDeskKkm;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "payment_terminal_id")
    private PayTerminal payTerminal;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "division_id")
    private Division division;

}
