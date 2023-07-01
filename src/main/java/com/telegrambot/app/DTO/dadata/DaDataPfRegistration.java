package com.telegrambot.app.DTO.dadata;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DaDataPfRegistration {
    private String type;
    private String series;
    private String number;
    @JsonProperty("issue_date")
    private long issueDate;
    @JsonProperty("issue_authority")
    private String issueAuthority;
}
