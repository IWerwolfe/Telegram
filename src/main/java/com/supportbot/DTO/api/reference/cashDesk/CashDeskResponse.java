package com.supportbot.DTO.api.reference.cashDesk;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.supportbot.DTO.api.typeObjects.EntityResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CashDeskResponse extends EntityResponse {

    @JsonCreator
    public CashDeskResponse(String json) {
        fillToJson(json, CashDeskResponse.class, this);
    }
}
