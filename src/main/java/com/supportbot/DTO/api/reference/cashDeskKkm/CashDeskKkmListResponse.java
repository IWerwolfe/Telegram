package com.supportbot.DTO.api.reference.cashDeskKkm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.supportbot.DTO.api.typeОbjects.DataListResponse;
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
