package com.telegrambot.app.DTO.api.manager;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api.DataListResponse;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ManagerDataListResponse extends DataListResponse<ManagerResponse> {
    public ManagerDataListResponse(boolean result, String error) {
        super(result, error);
    }
}
