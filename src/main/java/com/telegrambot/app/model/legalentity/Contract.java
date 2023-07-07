package com.telegrambot.app.model.legalentity;

import com.telegrambot.app.DTO.BillingType;
import com.telegrambot.app.model.EntityBD_1C;
import com.telegrambot.app.model.EntityDefaults;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "contracts")
public class Contract extends EntityBD_1C {

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
        setGuid(guid);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(getName());
        if (getDate() != null) {
            builder.append(" от ").append(getDate().format(FORMATTER_DATE));
        }
        return builder.toString();
    }
}
