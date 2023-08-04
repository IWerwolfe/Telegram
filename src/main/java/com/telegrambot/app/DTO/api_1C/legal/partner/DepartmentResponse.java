package com.telegrambot.app.DTO.api_1C.legal.partner;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api_1C.typeОbjects.Entity1C;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class DepartmentResponse extends Entity1C {
    private String guidPartner;
    private String guidContract;
    private Boolean isBilling;
    private Boolean isExcusableGoods;
    private Boolean isMarkedGoods;
    private Boolean isEGAIS;
}
