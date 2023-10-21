package com.telegrambot.app.DTO.api.reference.legal.division;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api.typeОbjects.DataListResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DivisionDataListResponse extends DataListResponse<DivisionResponse> {
    public DivisionDataListResponse(boolean result, String error) {
        super(result, error);
    }
}
