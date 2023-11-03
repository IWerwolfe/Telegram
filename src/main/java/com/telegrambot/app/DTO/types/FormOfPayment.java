package com.telegrambot.app.DTO.types;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FormOfPayment {
    CARD("Банковской картой"),
    INVOICE("Счет на оплату"),
    SBP("Система быстрых платежей");

    private final String label;

    @Override
    public String toString() {
        return getLabel();
    }
}
