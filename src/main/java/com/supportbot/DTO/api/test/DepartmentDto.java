package com.supportbot.DTO.api.test;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class DepartmentDto {
    private String code;
    private String guid;
    private String name;
    private String guidPartner;
    private String guidContract;
    private Boolean isBilling;
    private Boolean isExcisableGoods;
    private Boolean isMarkedGoods;
    private Boolean isEgais;
}
