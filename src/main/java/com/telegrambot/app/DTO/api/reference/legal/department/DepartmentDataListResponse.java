package com.telegrambot.app.DTO.api.reference.legal.department;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api.typeОbjects.DataListResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DepartmentDataListResponse extends DataListResponse<DepartmentResponse> {
    public DepartmentDataListResponse(boolean result, String error) {
        super(result, error);
    }
}
