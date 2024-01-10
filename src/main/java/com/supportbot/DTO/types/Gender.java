package com.supportbot.DTO.types;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Gender {
    MALE("Мужской"),
    FEMALE("Женский");

    private String label;

    @Override
    public String toString() {
        return getLabel();
    }
}
