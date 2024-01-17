package com.supportbot.DTO.api.typeObjects;

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

    public DataListResponse(boolean result, String error, List<T> list) {
        super(result, error);
        this.list = list;
    }

    public DataListResponse(Boolean result, String error, List<T> list) {
        super(result, error);
        this.list = list;
    }

    public DataListResponse(Boolean result, String error) {
        super(result, error);
    }

}
