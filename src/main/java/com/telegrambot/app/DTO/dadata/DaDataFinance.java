package com.telegrambot.app.DTO.dadata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DaDataFinance {

    @JsonProperty("tax_system")
    private String taxSystem;
    @JsonProperty("income")
    private double income;
    @JsonProperty("expense")
    private double expense;
    @JsonProperty("debt")
    private String debt;
    @JsonProperty("penalty")
    private String penalty;
    @JsonProperty("year")
    private int year;
}
