package com.supportbot.DTO.dadata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DaDataName {

    @JsonProperty("full_with_opf")
    private String fullWithOpf;
    @JsonProperty("short_with_opf")
    private String shortWithOpf;
    private String latin;
    private String full;
    private String shorts;
}
