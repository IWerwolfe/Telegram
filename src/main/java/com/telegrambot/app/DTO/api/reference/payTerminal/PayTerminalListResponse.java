package com.telegrambot.app.DTO.api.reference.payTerminal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api.typeОbjects.DataListResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PayTerminalListResponse extends DataListResponse<PayTerminalResponse> {
    public PayTerminalListResponse(boolean result, String error) {
        super(result, error);
    }
}
