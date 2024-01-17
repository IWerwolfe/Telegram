package com.supportbot.DTO.api.reference.payTerminal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.supportbot.DTO.api.typeObjects.EntityResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PayTerminalResponse extends EntityResponse {

    @JsonCreator
    public PayTerminalResponse(String json) {
        fillToJson(json, PayTerminalResponse.class, this);
    }
}
