package com.telegrambot.app.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FormOfPayment {
    CARD("Банковской картой"),
    INVOICE("Счет на оплату"),
    CRYPTO("Крипровалюта");

    private String label;
}
