package com.supportbot.model.reference.legalentity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@DiscriminatorValue("company")
public class Company extends LegalEntity {

    public Company(String guid) {
        super(guid);
    }

    public Company(String guid, String code) {
        super(guid, code);
    }
}
