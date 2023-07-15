package com.telegrambot.app.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PaymentType {
    INCOMING("Поступление денег"),
    EXPENSE("Расход денег"),
    REFUND("Возврат денег");

    private String label;
}
