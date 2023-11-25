package com.telegrambot.app.model.balance;

public interface EntityBalance {

    int getBalance();

    void setBalance(int amount);

    default void updateBalance(int sum) {
        setBalance(getBalance() + sum);
    }
}
