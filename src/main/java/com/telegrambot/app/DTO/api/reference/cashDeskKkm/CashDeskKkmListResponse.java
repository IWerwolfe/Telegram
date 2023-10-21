package com.telegrambot.app.DTO.api.reference.cashDeskKkm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api.typeОbjects.DataListResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CashDeskKkmListResponse extends DataListResponse<CashDeskKkmResponse> {
    public CashDeskKkmListResponse(boolean result, String error) {
        super(result, error);
    }
}
