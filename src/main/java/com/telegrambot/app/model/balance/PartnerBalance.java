package com.telegrambot.app.model.balance;

import com.telegrambot.app.model.reference.legalentity.Partner;
import com.telegrambot.app.model.types.Balance;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "partner_balances")
@NoArgsConstructor
public class PartnerBalance extends Balance {

    @OneToOne
    @JoinColumn(name = "partner_id")
    private Partner partner;

    public PartnerBalance(Partner partner) {
        this.partner = partner;
        setAmount(0);
    }
}
