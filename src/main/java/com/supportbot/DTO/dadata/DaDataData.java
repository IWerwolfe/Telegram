package com.supportbot.DTO.dadata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DaDataData {

    private String value;
    @JsonProperty("unrestricted_value")
    private String unrestrictedValue;
    private DaDataParty data;
}
