package com.supportbot.DTO.dadata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DaDataAuthorities {

    @JsonProperty("fts_registration")
    private DaDataFtsRegistration ftsRegistration;
    @JsonProperty("fts_report")
    private DaDataFtsReport ftsReport;
    @JsonProperty("pf")
    private DaDataPf pf;
    @JsonProperty("sif")
    private DaDataSif sif;
}
