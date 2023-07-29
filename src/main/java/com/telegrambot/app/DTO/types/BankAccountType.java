package com.telegrambot.app.DTO.types;

public enum BankAccountType {
    CHECKING("Checking Account"),
    SAVINGS("Savings Account"),
    MONEY_MARKET("Money Market Account"),
    CERTIFICATE_OF_DEPOSIT("Certificate of Deposit"),
    LOAN("Loan Account"),
    CREDIT_CARD("Credit Card Account");

    private final String label;

    BankAccountType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
