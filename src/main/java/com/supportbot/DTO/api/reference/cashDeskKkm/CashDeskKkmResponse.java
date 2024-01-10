package com.supportbot.DTO.api.reference.cashDeskKkm;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.supportbot.DTO.api.typeОbjects.EntityResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CashDeskKkmResponse extends EntityResponse {

    @JsonCreator
    public CashDeskKkmResponse(String json) {
        fillToJson(json, CashDeskKkmResponse.class, this);
    }
}
