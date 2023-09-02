package com.telegrambot.app.model.reference.legalentity;

import com.telegrambot.app.model.documents.docdata.SyncData;
import com.telegrambot.app.model.types.Reference;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "departments")
@NoArgsConstructor
public class Department extends Reference {

    @ManyToOne
    @JoinColumn(name = "partner_id")
    private Partner partner;
    @ManyToOne
    @JoinColumn(name = "contract_id")
    private Contract contract;
    @JoinColumn(name = "is_billing")
    private Boolean isBilling = false;
    @JoinColumn(name = "is_excusable_goods")
    private Boolean isExcisableGoods = false;
    @JoinColumn(name = "is_marked_goods")
    private Boolean isMarkedGoods = false;
    @JoinColumn(name = "is_egais")
    private Boolean isEgais = false;

    public Department(String guid) {
        this.setSyncData(new SyncData(guid));
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
