package com.supportbot.DTO.dadata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DaDataSif {

    private String type;
    private String code;
    private String name;
    private String address;
}
