package com.telegrambot.app.model.types;

import com.telegrambot.app.services.converter.Converter;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

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
        if (amount != null) {
            this.amount = amount;
            this.setDate(System.currentTimeMillis());
        }
    }

    @Override
    public String toString() {
        String string = "";
        if (this.date != null) {
            LocalDateTime date = Converter.convertLongToLocalDateTime(this.date);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.getDefault());
            string = date == null ? "" : "Обновлен " + date.format(formatter);
        }
        return "Ваш баланс " + amount + System.lineSeparator() + string;
    }
}
