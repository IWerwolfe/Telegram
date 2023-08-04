package com.telegrambot.app.DTO.api_1C.legal.partner;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api_1C.typeОbjects.Entity1C;
import lombok.Data;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ContractResponse extends Entity1C {
    private String guidPartner;
    @JsonFormat(pattern = DATE_PATTERN)
    private Date startBilling;
    @JsonFormat(pattern = DATE_PATTERN)
    private Date date;
    @JsonFormat(pattern = DATE_PATTERN)
    private Date stopBilling;
    private Float standardHourlyRate;
    private Boolean isBilling;
    private String billingTypeString;
}
