package com.telegrambot.app.DTO.dadata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DaDataResponse {

    private DaDataData[] suggestions;
}
