package com.telegrambot.app.DTO.api_1C.typesОbjects;

import lombok.Data;

@Data
public class Entity1C {
    public final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
    private String code;
    private String guid;
    private String name;
}
