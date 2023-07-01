package com.telegrambot.app.DTO;

import lombok.Data;

@Data
public abstract class DataResponse {
    private boolean result;
    private String error;
}
