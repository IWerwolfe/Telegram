package com.telegrambot.app.DTO.api.reference.cashDesk;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api.typeОbjects.EntityResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CashDeskResponse extends EntityResponse {

    @JsonCreator
    public CashDeskResponse(String json) {
        createToJson(json, CashDeskResponse.class, this);
    }
}
