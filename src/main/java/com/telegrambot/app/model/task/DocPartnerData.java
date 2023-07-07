package com.telegrambot.app.model.task;

import com.telegrambot.app.model.legalentity.Contract;
import com.telegrambot.app.model.legalentity.Department;
import com.telegrambot.app.model.legalentity.Partner;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        if (partner != null) {
            builder.append(partner);
            builder.append(System.lineSeparator())
                    .append("Договор: ")
                    .append(contract == null ? "Основной" : contract.toString());

            if (department != null) {
                builder.append(System.lineSeparator())
                        .append(department);
            }

        } else {
            builder.append("Контрагент не указан");
        }
        return builder.toString();
    }
}
