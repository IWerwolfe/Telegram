package com.telegrambot.app.DTO.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataEntityResponse<T> extends DataResponse {
    private T entity;

    public DataEntityResponse(boolean result, String error) {
        super(result, error);
    }
}
