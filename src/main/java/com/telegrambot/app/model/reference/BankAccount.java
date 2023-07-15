package com.telegrambot.app.model.reference;

import com.telegrambot.app.model.BankAccountType;
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

    private String currency;
    @ManyToOne
    @JoinColumn(name = "bank_id")
    private Bank bank;
    @ManyToOne
    @JoinColumn(name = "payment_bank_id")
    private Bank paymentBank;
    private Long number;
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
}
