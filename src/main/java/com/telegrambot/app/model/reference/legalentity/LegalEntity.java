package com.telegrambot.app.model.reference.legalentity;

import com.telegrambot.app.model.documents.docdata.SyncData;
import com.telegrambot.app.model.reference.BankAccount;
import com.telegrambot.app.model.types.Reference;
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
    @ManyToOne
    @JoinColumn(name = "default_contract_id")
    private Contract defaultContract;

    public LegalEntity(String guid) {
        this.setSyncData(new SyncData(guid));
    }

    @Override
    public String toString() {
        return getName() + ", ИНН " + inn + (kpp != null ? " КПП " + kpp : "");
    }
}
