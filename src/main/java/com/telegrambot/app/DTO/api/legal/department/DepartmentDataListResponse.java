package com.telegrambot.app.DTO.api.legal.department;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api.DataListResponse;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DepartmentDataListResponse extends DataListResponse<DepartmentResponse> {
    public DepartmentDataListResponse(boolean result, String error) {
        super(result, error);
    }
}
