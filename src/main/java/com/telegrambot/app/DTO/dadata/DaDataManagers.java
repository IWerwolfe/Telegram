package com.telegrambot.app.DTO.dadata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DaDataManagers {

    private String inn;
    private DaDataFio fio;
    private String post;
    private String hid;
    private String type;
}
