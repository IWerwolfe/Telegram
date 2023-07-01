package com.telegrambot.app.model.legalentity;

import com.telegrambot.app.DTO.PartnerType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@DiscriminatorValue("partners")
@NoArgsConstructor
public class Partner extends LegalEntity {
    private PartnerType partnerType;

    public Partner(String guid) {
        super(guid);
    }
}
