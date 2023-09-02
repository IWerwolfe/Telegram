package com.telegrambot.app.DTO.api.reference.legal.department;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api.typeОbjects.EntityResponse;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class DepartmentResponse extends EntityResponse {
    private String guidPartner;
    private String guidContract;
    private Boolean isBilling;
    private Boolean isExcisableGoods;
    private Boolean isMarkedGoods;
    private Boolean isEgais;
}
