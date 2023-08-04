package com.telegrambot.app.DTO.api_1C.taskResponse;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api_1C.typeОbjects.Entity1C;
import lombok.Data;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class TaskResponse extends Entity1C {

    private String description;
    private String decision;
    private String guidStatus;
    private String guidPartner;
    private String guidDepartment;
    private String guidDivision;
    private String guidContract;
    private String guidManager;
    private String guidAuthor;
    private String comment;
    @JsonFormat(pattern = DATE_PATTERN)
    private Date closingDate;
    @JsonFormat(pattern = DATE_PATTERN)
    private Date date;
    private String type;
    private Boolean isOutsourcing;
    private Boolean highPriority;
    private Boolean isBilling;
    private String totalAmount;
}
