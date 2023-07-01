package com.telegrambot.app.DTO.dadata;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DaDataEmails {
    @JsonProperty("value")
    private String value;
    @JsonProperty("unrestricted_value")
    private String unrestrictedValue;
    @JsonProperty("data")
    private DaDataEmailData data;
}
