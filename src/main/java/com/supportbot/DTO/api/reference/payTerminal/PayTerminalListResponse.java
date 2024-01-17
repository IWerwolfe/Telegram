package com.supportbot.DTO.api.reference.payTerminal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.supportbot.DTO.api.typeObjects.DataListResponse;
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
