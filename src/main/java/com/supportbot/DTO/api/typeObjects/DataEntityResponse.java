package com.supportbot.DTO.api.typeObjects;

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

    public DataEntityResponse(boolean result, String error, T entity) {
        super(result, error);
        this.entity = entity;
    }

    public DataEntityResponse(Boolean result, String error, T entity) {
        super(result, error);
        this.entity = entity;
    }

    public DataEntityResponse(Boolean result, String error) {
        super(result, error);
    }
}
