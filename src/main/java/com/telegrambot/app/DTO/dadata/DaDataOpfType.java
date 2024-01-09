package com.telegrambot.app.DTO.dadata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DaDataOpfType {

    @JsonProperty("code")
    private String code;
    @JsonProperty("full")
    private String full;
    @JsonProperty("short_name")
    private String shortName;
    @JsonProperty("full_name")
    private String fullName;
}
