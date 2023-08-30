package com.telegrambot.app.model.transaction;

import com.telegrambot.app.model.legalentity.Department;
import com.telegrambot.app.model.legalentity.Partner;
import com.telegrambot.app.model.user.UserBD;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "financial_transactions")
public class FinTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long date;
    @ManyToOne
    @JoinColumn(name = "legal_id")
    private Partner partner;
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserBD user;
    @JoinColumn(name = "basis_doc_id")
    private Long IdBasicDoc;
    private Integer amount;
}
