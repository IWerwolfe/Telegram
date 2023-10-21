package com.telegrambot.app.DTO.api.reference.cashDeskKkm;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api.typeОbjects.EntityResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CashDeskKkmResponse extends EntityResponse {

    @JsonCreator
    public CashDeskKkmResponse(String json) {
        createToJson(json, CashDeskKkmResponse.class, this);
    }
}
