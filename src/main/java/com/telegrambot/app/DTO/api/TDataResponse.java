package com.telegrambot.app.DTO.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api.typeОbjects.Entity1C;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TDataResponse<T extends Entity1C> extends DataResponse {
    private T entity;

    public TDataResponse(boolean b, String dataNotFound) {
        this.setResult(b);
        this.setError(dataNotFound);
    }
}
