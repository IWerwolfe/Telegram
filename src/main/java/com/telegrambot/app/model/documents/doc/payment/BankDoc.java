package com.telegrambot.app.model.documents.doc.payment;

import com.telegrambot.app.model.documents.docdata.IncomingData;
import com.telegrambot.app.model.documents.doctype.PayDoc;
import com.telegrambot.app.model.reference.BankAccount;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "bank_docs")
@NoArgsConstructor
public class BankDoc extends PayDoc {

    private static String descriptor;

    @ManyToOne
    @JoinColumn(name = "bank_account_id")
    private BankAccount bankAccount;
    @ManyToOne
    @JoinColumn(name = "payer_bank_account_id")
    private BankAccount payerBankAccount;
    @JoinColumn(name = "incoming_data")
    private IncomingData incomingData;
    @JoinColumn(name = "payment_purpose")
    private String paymentPurpose;
    @Column(precision = 8, scale = 3)
    private BigDecimal commission;
    @JoinColumn(name = "decoding_filling_option")
    private String decodingFillingOption;

    @Override
    protected String getDescriptor() {
        return "Банковский документ";
    }
}
