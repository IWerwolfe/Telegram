package com.supportbot.DTO.api.reference.bank;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.supportbot.DTO.api.typeОbjects.EntityResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BankResponse extends EntityResponse {

    private String corrAccount;
    private String city;
    private String address;
    private String phone;
    private String swift;
    private String country;

    @JsonCreator
    public BankResponse(String json) {
        fillToJson(json, BankResponse.class, this);
    }
}
