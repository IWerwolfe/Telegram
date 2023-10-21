package com.telegrambot.app.model.documents.docdata;

import com.telegrambot.app.model.reference.legalentity.Contract;
import com.telegrambot.app.model.reference.legalentity.Department;
import com.telegrambot.app.model.reference.legalentity.Partner;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class PartnerData {

    @ManyToOne()
    @JoinColumn(name = "partner_id")
    private Partner partner;

    @ManyToOne()
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne()
    @JoinColumn(name = "contract_id")
    private Contract contract;

    public PartnerData(Partner partner) {
        this.partner = partner;
        this.contract = partner.getDefaultContract();
        this.department = partner.getDepartments().size() == 1 ? partner.getDepartments().get(0) : null;
    }

    @Override
    public String toString() {

        if (partner == null) {
            return "Контрагент не указан";
        }

        StringBuilder builder = new StringBuilder();
        builder.append(partner);
        builder.append(System.lineSeparator())
                .append("Договор: ")
                .append(contract == null ? "Основной" : contract.toString());

        if (department != null) {
            builder.append(System.lineSeparator())
                    .append(department);
        }

        return builder.toString();
    }
}
