package com.telegrambot.app.DTO.api.typeОbjects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EntityResponse {
    public final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
    private String code;
    private String guid;
    private String name;
    private Boolean markedForDel;
}
