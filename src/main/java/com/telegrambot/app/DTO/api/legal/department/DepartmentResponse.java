package com.telegrambot.app.DTO.api.legal.department;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api.typeОbjects.Entity1C;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class DepartmentResponse extends Entity1C {
    private String guidPartner;
    private String guidContract;
    private Boolean isBilling;
    private Boolean isExcisableGoods;
    private Boolean isMarkedGoods;
    private Boolean isEgais;
}
