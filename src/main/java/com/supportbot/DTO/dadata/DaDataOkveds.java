package com.supportbot.DTO.dadata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DaDataOkveds {

    private boolean main;
    private String type;
    private String code;
    private String name;
}
