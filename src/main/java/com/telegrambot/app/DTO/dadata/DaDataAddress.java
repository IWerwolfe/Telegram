package com.telegrambot.app.DTO.dadata;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DaDataAddress {
    private String value;
    @JsonProperty("unrestricted_value")
    private String unrestrictedValue;
    private DaDataAddressData data;
}
