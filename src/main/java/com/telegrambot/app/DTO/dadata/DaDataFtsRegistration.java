package com.telegrambot.app.DTO.dadata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DaDataFtsRegistration {

    private String type;
    private String code;
    private String name;
    private String address;
}
