package com.telegrambot.app.DTO.api.reference.payTerminal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api.typeОbjects.EntityResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PayTerminalResponse extends EntityResponse {

    @JsonCreator
    public PayTerminalResponse(String json) {
        createToJson(json, PayTerminalResponse.class, this);
    }
}
