package com.telegrambot.app.model.legalentity;

import com.telegrambot.app.DTO.BillingType;
import com.telegrambot.app.model.EntityBD_1C;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
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

    public Contract(String guid) {
        setGuid(guid);
    }
}
