package com.telegrambot.app.model.documents.doc.payment;

import com.telegrambot.app.model.documents.docdata.CardData;
import com.telegrambot.app.model.documents.docdata.FiscalData;
import com.telegrambot.app.model.reference.BankAccount;
import com.telegrambot.app.model.reference.PayTerminal;
import com.telegrambot.app.model.types.doctype.PayDoc;
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
@Table(name = "card_docs")
@NoArgsConstructor
public class CardDoc extends PayDoc {

    @ManyToOne
    @JoinColumn(name = "bank_account_id")
    private BankAccount bankAccount;
    @ManyToOne
    @JoinColumn(name = "payment_terminal")
    private PayTerminal payTerminal;
    @JoinColumn(name = "fiscal_data")
    private FiscalData fiscalData;
    @JoinColumn(name = "ticket_number")
    private Long ticketNumber;
    @JoinColumn(name = "commission_percentage")
    private Long commissionPercentage;
    @JoinColumn(name = "reference_number")
    private String referenceNumber;
    private Integer commission;
    @JoinColumn(name = "card_data")
    private CardData cardData;

    @Override
    protected String getDescriptor() {
        return "Операция по карте";
    }
}
