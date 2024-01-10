package com.supportbot.DTO.types;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FormOfPayment {
    CARD("Банковская карта"),
    INVOICE("Счет на оплату"),
    SBP("Система быстрых платежей"),
    SBP_STATIC("Система быстрых платежей (ссылка)"),
    CRYPTO("Криптовалюта");

    private final String label;

    @Override
    public String toString() {
        return getLabel();
    }
}
