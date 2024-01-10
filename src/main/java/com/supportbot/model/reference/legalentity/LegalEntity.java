package com.supportbot.model.reference.legalentity;

import com.supportbot.model.reference.BankAccount;
import com.supportbot.model.types.Reference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Entity
@DiscriminatorColumn(name = "legal_type")
public abstract class LegalEntity extends Reference {

    private String inn;
    private String kpp;
    @ManyToOne
    @JoinColumn(name = "bank_account_id")
    private BankAccount bankAccount;
    private String comment;
    private String OGRN;
    private LocalDateTime commencement;
    private String certificate;
    private LocalDateTime dateCertificate;
    private String OKPO;

    public LegalEntity() {
    }

    public LegalEntity(String guid) {
        super(guid);
    }

    public LegalEntity(String guid, String code) {
        super(guid, code);
    }

    @Override
    public String toString() {
        return getName() + ", ИНН " + inn + (kpp != null ? " КПП " + kpp : "");
    }
}
