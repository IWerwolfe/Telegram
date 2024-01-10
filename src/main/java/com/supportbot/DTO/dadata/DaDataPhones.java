package com.supportbot.DTO.dadata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DaDataPhones {

    private String value;
    @JsonProperty("unrestricted_value")
    private String unrestrictedValue;
    private DaDataPhoneData data;
}
