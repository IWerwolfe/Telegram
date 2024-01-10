package com.supportbot.DTO.types;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PartnerType {
    BUYER("Покупатель"),
    VENDOR("Поставщик"),
    OTHER("Прочее");

    private String label;

    @Override
    public String toString() {
        return getLabel();
    }
}
