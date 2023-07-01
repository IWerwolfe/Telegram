package com.telegrambot.app.model.task;

import com.telegrambot.app.model.legalentity.Contract;
import com.telegrambot.app.model.legalentity.Department;
import com.telegrambot.app.model.legalentity.Partner;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Embeddable
public class DocPartnerData {
    @ManyToOne
    @JoinColumn(name = "partner_id")
    private Partner partner;
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
    @ManyToOne
    @JoinColumn(name = "contract_id")
    private Contract contract;
}
