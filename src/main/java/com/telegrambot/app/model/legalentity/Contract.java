package com.telegrambot.app.model.legalentity;

import com.telegrambot.app.DTO.types.BillingType;
import com.telegrambot.app.model.EntityDefaults;
import com.telegrambot.app.model.documents.docdata.SyncData;
import com.telegrambot.app.model.reference.Reference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "contracts")
public class Contract extends Reference {

    private LocalDateTime date;
    private boolean isBilling;
    private LocalDateTime startBilling;
    private LocalDateTime stopBilling;
    private float standardHourlyRate;
    private BillingType billingType;
    @ManyToOne
    @JoinColumn(name = "partner_id")
    private Partner partner;

    public Contract(Partner partner) {
        this.partner = partner;
    }

    @PrePersist
    private void prePersist() {
        EntityDefaults.initializeDefaultContract(this);
    }

    public Contract(String guid) {
        this.setSyncData(new SyncData(guid));
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(getName());
        if (getDate() != null) {
            builder.append(" от ").append(getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
        }
        return builder.toString();
    }
}
