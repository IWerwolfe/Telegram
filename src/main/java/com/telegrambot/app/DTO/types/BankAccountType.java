package com.telegrambot.app.DTO.types;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BankAccountType {
    Checking("Сберегательный счёт"),
    Savings("Вклад"),
    CreditCard("Кредитная карта"),
    DebitCard("Дебетовая карта"),
    MoneyMarket("Маржинальный счёт"),
    BusinessChecking("Бизнес-счёт"),
    BusinessSavings("Бизнес-вклад"),
    BusinessCreditCard("Бизнес-кредитная карта"),
    BusinessDebitCard("Бизнес-дебетовая карта"),
    BusinessMoneyMarket("Бизнес-маржинальный счёт");

    private String label;

    @Override
    public String toString() {
        return getLabel();
    }
}
