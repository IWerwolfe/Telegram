package com.telegrambot.app.model.legalentity;

import com.telegrambot.app.model.EntityBD_1C;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Entity
@NoArgsConstructor
@DiscriminatorColumn(name = "legal_entity_type")
public abstract class LegalEntity extends EntityBD_1C {

    private String inn;
    private String kpp;
    private String bankAccount;
    private String comment;
    private String OGRN;
    private LocalDateTime commencement;
    private String certificate;
    private LocalDateTime dateCertificate;
    private String OKPO;
    @ManyToOne
    @JoinColumn(name = "default_contract_id")
    private Contract defaultContract;

    public LegalEntity(String guid) {
        setGuid(guid);
    }

    @Override
    public String toString() {
        return getName() + ", ИНН " + inn + (kpp != null ? " КПП " + kpp : "");
    }
}
