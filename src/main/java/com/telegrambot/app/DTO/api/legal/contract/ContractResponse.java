package com.telegrambot.app.DTO.api.legal.contract;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telegrambot.app.DTO.api.typeОbjects.Entity1C;
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
    private String standardHourlyRate;
    private Boolean isBilling;
    private String billingTypeString;
}
