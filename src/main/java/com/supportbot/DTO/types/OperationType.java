package com.supportbot.DTO.types;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OperationType {

    CREATE("Создание"),
    UPDATE("Обновление"),
    EDIT("Редактирование"),
    DEL("Удаление");

    private final String label;

    @Override
    public String toString() {
        return getLabel();
    }
}
