package com.supportbot.model.types.doctype;

import com.supportbot.DTO.types.PaymentType;
import com.supportbot.model.reference.CashDesk;
import com.supportbot.model.reference.CashFlowItem;
import com.supportbot.model.types.Document;
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

    @Override
    public Integer getTransactionAmount() {
        return getTotalAmount() * (getPaymentType() == PaymentType.INCOMING ? 1 : -1);
    }
}
