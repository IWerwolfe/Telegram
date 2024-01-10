package com.supportbot.DTO.api.reference.legal.department;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.supportbot.DTO.api.typeОbjects.EntityResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DepartmentResponse extends EntityResponse {
    private String guidPartner;
    private String guidContract;
    private Boolean isBilling;
    private Boolean isExcisableGoods;
    private Boolean isMarkedGoods;
    private Boolean isEgais;

    @JsonCreator
    public DepartmentResponse(String json) {
        fillToJson(json, DepartmentResponse.class, this);
    }
}
