package com.telegrambot.app.model.reference.legalentity;

import com.telegrambot.app.DTO.types.BillingType;
import com.telegrambot.app.model.types.Reference;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "contracts")
public class Contract extends Reference {

    private LocalDateTime date;
    private Boolean isBilling;
    private Boolean isDefault;
    private LocalDateTime startBilling;
    private LocalDateTime stopBilling;
    private Integer standardHourlyRate;
    private BillingType billingType;
    @ManyToOne
    @JoinColumn(name = "partner_id")
    private Partner partner;

    public Contract() {
        this.date = LocalDateTime.now();
        setName("Основной.");
        this.isBilling = false;
    }

    public Contract(String guid) {
        super(guid);
    }

    public Contract(String guid, String code) {
        super(guid, code);
    }

    public Contract(Partner partner) {
        this();
        this.partner = partner;
    }
}
