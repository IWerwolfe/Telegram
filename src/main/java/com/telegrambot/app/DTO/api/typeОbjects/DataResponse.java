package com.telegrambot.app.DTO.api.typeОbjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataResponse {
    private boolean result;
    private String error = "";

    public DataResponse(Boolean result, String error) {
        this.result = result != null && result;
        this.error = error;
    }
}
