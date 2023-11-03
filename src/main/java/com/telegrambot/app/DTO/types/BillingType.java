package com.telegrambot.app.DTO.types;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BillingType {
    HOURLY_PAYMENT("Почасовая оплата"),
    MONTHLY_PAYMENT("Оплата раз в месяц"),
    QUARTER_PAYMENT("Оплата раз в квартал"),
    YEAR_PAYMENT("Оплата раз в год");

    private String label;

    @Override
    public String toString() {
        return getLabel();
    }
}
