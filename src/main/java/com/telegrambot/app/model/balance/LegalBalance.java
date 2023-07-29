package com.telegrambot.app.model.balance;

import com.telegrambot.app.model.legalentity.LegalEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "legal_balances")
@NoArgsConstructor
public class LegalBalance extends Balance {
    @ManyToOne
    @JoinColumn(name = "legal_id")
    private LegalEntity legal;

    public LegalBalance(LegalEntity legal) {
        this.legal = legal;
    }
}
