package com.telegrambot.app.DTO.dadata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DaDataSifRegistration {

    private String type;
    private String series;
    private String number;
    @JsonProperty("issue_date")
    private long issueDate;
    @JsonProperty("issue_authority")
    private String issueAuthority;
}
