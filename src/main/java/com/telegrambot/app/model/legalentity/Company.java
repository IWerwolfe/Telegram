package com.telegrambot.app.model.legalentity;

import com.telegrambot.app.model.documents.docdata.SyncData;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@DiscriminatorValue("company")
public class Company extends LegalEntity {

    public Company(String guid) {
        setSyncData(new SyncData(guid));
    }

    public Company(String guid, String code) {
        setSyncData(new SyncData(guid, code));
    }
}
