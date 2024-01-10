package com.supportbot.model.reference;

import com.supportbot.DTO.types.BankAccountType;
import com.supportbot.model.reference.legalentity.LegalEntity;
import com.supportbot.model.types.Reference;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "bank_accounts")
@NoArgsConstructor
public class BankAccount extends Reference {

    @ManyToOne
    @JoinColumn(name = "legal_id")
    private LegalEntity legal;
    private String currency;
    @ManyToOne
    @JoinColumn(name = "bank_id")
    private Bank bank;
    @ManyToOne
    @JoinColumn(name = "payment_bank_id")
    private Bank paymentBank;
    private String number;
    private BankAccountType type;
    private String correspondent;
    @JoinColumn(name = "payment_purpose")
    private String paymentPurpose;
    @JoinColumn(name = "permit_number_and_date")
    private String permitNumberAndDate;
    @JoinColumn(name = "opening_date")
    private LocalDateTime openingDate;
    @JoinColumn(name = "closing_date")
    private LocalDateTime closingDate;

    public BankAccount(String guid) {
        super(guid);
    }

    public BankAccount(String guid, String code) {
        super(guid, code);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
