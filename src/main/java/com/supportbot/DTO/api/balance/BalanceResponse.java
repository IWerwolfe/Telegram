package com.supportbot.DTO.api.balance;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.supportbot.DTO.api.typeОbjects.EntityResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BalanceResponse {
    private String guid;
    private String inn;
    private String kpp;
    private String amount;

    @JsonCreator
    public BalanceResponse(String json) {
        EntityResponse.fillToJson(json, BalanceResponse.class, this);
    }
}
