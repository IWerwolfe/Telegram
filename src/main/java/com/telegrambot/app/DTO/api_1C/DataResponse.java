package com.telegrambot.app.DTO.api_1C;

import com.telegrambot.app.DTO.api_1C.typesОbjects.Entity1C;
import lombok.Data;

@Data
public abstract class DataResponse extends Entity1C {
    private boolean result;
    private String error;
}
