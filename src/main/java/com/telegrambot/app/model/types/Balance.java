package com.telegrambot.app.model.types;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class Balance {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long date;
    private Integer amount = 0;

    public void setAmount(Integer amount) {
        this.amount = amount;
        this.setDate(System.currentTimeMillis());
    }
}
