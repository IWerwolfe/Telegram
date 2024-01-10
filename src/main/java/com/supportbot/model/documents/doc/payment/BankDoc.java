package com.supportbot.model.documents.doc.payment;

import com.supportbot.model.documents.docdata.IncomingData;
import com.supportbot.model.reference.BankAccount;
import com.supportbot.model.types.doctype.PayDoc;
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
    private Integer commission;
    @JoinColumn(name = "decoding_filling_option")
    private String decodingFillingOption;

    @Override
    protected String getDescriptor() {
        return "Банковский документ";
    }
}
