package com.telegrambot.app.DTO.api_1C.test;

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
    private Boolean isExcusableGoods;
    private Boolean isMarkedGoods;
    private Boolean isEGAIS;
}
