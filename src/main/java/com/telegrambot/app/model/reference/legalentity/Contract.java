package com.telegrambot.app.model.reference.legalentity;

import com.telegrambot.app.DTO.types.BillingType;
import com.telegrambot.app.model.EntityDefaults;
import com.telegrambot.app.model.documents.docdata.SyncData;
import com.telegrambot.app.model.types.Reference;
import jakarta.persistence.*;
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
        setName("Основной");
    }

    public Contract(Partner partner) {
        this();
        this.partner = partner;
    }

    @PrePersist
    private void prePersist() {
        EntityDefaults.initializeDefaultContract(this);
    }

    public Contract(String guid) {
        this.setSyncData(new SyncData(guid));
    }
}
