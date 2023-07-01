package com.telegrambot.app.DTO.api_1C;

import lombok.Data;

@Data
public class Entity1C {
    final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
    private String code;
    private String guid;
    private String name;
}
