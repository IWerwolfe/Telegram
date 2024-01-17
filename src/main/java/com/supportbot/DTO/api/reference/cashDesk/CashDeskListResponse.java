package com.supportbot.DTO.api.reference.cashDesk;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.supportbot.DTO.api.typeObjects.DataListResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CashDeskListResponse extends DataListResponse<CashDeskResponse> {
    public CashDeskListResponse(boolean result, String error) {
        super(result, error);
    }
}
