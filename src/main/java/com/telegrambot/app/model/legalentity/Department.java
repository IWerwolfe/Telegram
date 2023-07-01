package com.telegrambot.app.model.legalentity;

import com.telegrambot.app.model.EntityBD_1C;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity(name = "departments")
public class Department extends EntityBD_1C {

    @ManyToOne
    @JoinColumn(name = "partner_id")
    private Partner partner;
    @ManyToOne
    @JoinColumn(name = "contract_id")
    private Contract contract;
    private boolean isBilling;
    private boolean isExcusableGoods;
    private boolean isMarkedGoods;
    private boolean isEGAIS;
}
