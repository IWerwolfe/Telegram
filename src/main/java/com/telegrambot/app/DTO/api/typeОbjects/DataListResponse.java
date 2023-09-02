package com.telegrambot.app.DTO.api.typeОbjects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataListResponse<T> extends DataResponse {
    private List<T> list;

    public DataListResponse(boolean result, String error) {
        super(result, error);
    }
}
