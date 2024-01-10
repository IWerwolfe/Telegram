package com.supportbot.model.types;

import com.supportbot.services.converter.Converter;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class BalanceTest {

    @Test
    public void testSetAmount() {
        Balance balance = new MockBalance();
        assertNull(balance.getDate());

        Integer amount = 100;
        balance.setAmount(amount);

        assertEquals(amount, balance.getAmount());
        assertNotNull(balance.getDate());
    }

    @Test
    public void testToString() {
        Balance balance = new MockBalance();
        Integer amount = 200;
        balance.setAmount(amount);

        LocalDateTime date = Converter.convertLongToLocalDateTime(balance.getDate());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.getDefault());

        String expected = "Ваш баланс " + amount + System.lineSeparator() +
                "Обновлен " + date.format(formatter);

        assertEquals(expected, balance.toString());
    }

    private static class MockBalance extends Balance {
        public String getDateAsString() {
            if (getDate() != null) {
                return Converter.convertLongToLocalDateTime(getDate())
                        .format(DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.getDefault()));
            }
            return "";
        }
    }

}