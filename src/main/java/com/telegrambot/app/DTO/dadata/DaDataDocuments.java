package com.telegrambot.app.DTO.dadata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DaDataDocuments {

    @JsonProperty("fts_registration")
    private DaDataFtsRegistration ftsRegistration;
    @JsonProperty("fts_report")
    private DaDataFtsReport ftsReport;
    @JsonProperty("pf_registration")
    private DaDataPfRegistration pfRegistration;
    @JsonProperty("sif_registration")
    private DaDataSifRegistration sifRegistration;
    private DaDataSmb smb;
}
