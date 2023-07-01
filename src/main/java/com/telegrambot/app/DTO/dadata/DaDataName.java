package com.telegrambot.app.DTO.dadata;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DaDataName {
    @JsonProperty("full_with_opf")
    private String fullWithOpf;
    @JsonProperty("short_with_opf")
    private String shortWithOpf;
    private String latin;
    private String full;
    private String shorts;
}
