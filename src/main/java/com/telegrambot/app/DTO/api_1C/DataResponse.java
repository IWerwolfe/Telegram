package com.telegrambot.app.DTO.api_1C;

import lombok.Data;

@Data
public class DataResponse {
    private boolean result;
    private String error;

    public DataResponse(boolean result, String error) {
        this.result = result;
        this.error = error;
    }
}
