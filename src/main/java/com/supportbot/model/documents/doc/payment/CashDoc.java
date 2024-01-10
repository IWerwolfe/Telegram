package com.supportbot.model.documents.doc.payment;

import com.supportbot.model.documents.docdata.FiscalData;
import com.supportbot.model.types.doctype.PayDoc;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "cash_docs")
@NoArgsConstructor
public class CashDoc extends PayDoc {

    @JoinColumn(name = "fiscal_data")
    private FiscalData fiscalData;
    private String correspondence;
    @JoinColumn(name = "payment_basis")
    private String paymentBasis;

    @Override
    protected String getDescriptor() {
        return "Движение наличных";
    }
}
