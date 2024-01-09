package com.telegrambot.app.DTO.dadata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DaDataFio {

    private String surname;
    private String name;
    private String patronymic;
    private String gender;
    private String source;
    private String qc;
}
