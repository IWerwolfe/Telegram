package com.telegrambot.app.DTO.api.reference.bank;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api.typeОbjects.EntityResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BankResponse extends EntityResponse {

    private String correspondentAccount;
    private String city;
    private String address;
    private String phone;
    private String swift;
    private String country;

    @JsonCreator
    public BankResponse(String json) {
        createToJson(json, BankResponse.class, this);
    }
}
