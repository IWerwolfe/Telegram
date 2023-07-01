package com.telegrambot.app.DTO.dadata;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DaDataState {
    private String status;
    private String code;
    @JsonProperty("actuality_date")
    private long actualityDate;
    @JsonProperty("registration_date")
    private long registrationDate;
    @JsonProperty("liquidation_date")
    private long liquidationDate;
}
