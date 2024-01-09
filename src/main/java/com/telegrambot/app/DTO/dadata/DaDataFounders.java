package com.telegrambot.app.DTO.dadata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DaDataFounders {

    private String ogrn;
    private String inn;
    private String name;
    private String hid;
    private String type;
    private DaDataShare share;
}
