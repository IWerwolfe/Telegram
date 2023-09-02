package com.telegrambot.app.model.reference.legalentity;

import com.telegrambot.app.DTO.types.PartnerType;
import com.telegrambot.app.model.documents.docdata.SyncData;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("partners")
@NoArgsConstructor
public class Partner extends LegalEntity {

    private PartnerType partnerType;

    public Partner(String guid) {
        setSyncData(new SyncData(guid));
    }

    public Partner(String guid, String code) {
        setSyncData(new SyncData(guid, code));
    }
}
