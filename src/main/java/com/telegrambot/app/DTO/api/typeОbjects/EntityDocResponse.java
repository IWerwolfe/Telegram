package com.telegrambot.app.DTO.api.typeОbjects;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public abstract class EntityDocResponse extends EntityResponse {

    @JsonFormat(pattern = DATE_PATTERN)
    private Date date;
    private String comment;
    private String guidCompany;
    private String guidDepartment;
    private String guidPartner;
    private String guidContract;
    private String guidDivision;
    private String guidManager;
    private String totalAmount;
    private String guidCreator;
}
