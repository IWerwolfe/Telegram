package com.supportbot.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserType {
    UNAUTHORIZED("Неавторизованный"),
    USER("Пользователь"),
    ADMINISTRATOR("Администратор"),
    DIRECTOR("Владелец");

    private String label;

    @Override
    public String toString() {
        return getLabel();
    }
}
