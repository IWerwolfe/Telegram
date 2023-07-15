package com.telegrambot.app.model.documents.doctype;

import com.telegrambot.app.model.PaymentType;
import com.telegrambot.app.model.reference.CashDesk;
import com.telegrambot.app.model.reference.CashFlowItem;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class PayDoc extends Document {

    @ManyToOne
    @JoinColumn(name = "cash_desk_id")
    private CashDesk cashDesk;
    @ManyToOne
    @JoinColumn(name = "cash_flow_item_id")
    private CashFlowItem cashFlowItem;
    @JoinColumn(name = "payment_type")
    private PaymentType paymentType;

}
