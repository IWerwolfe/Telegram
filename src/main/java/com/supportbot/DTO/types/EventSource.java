package com.supportbot.DTO.types;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EventSource {
    API("Внешний сервис"),
    USER("Пользователь"),
    BOT("Бот");

    private String label;

    @Override
    public String toString() {
        return getLabel();
    }
}
