package com.supportbot.DTO.api.test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ContractDto {
    private String code;
    private String guid;
    private String name;
    private String guidPartner;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date startBilling;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Date stopBilling;
    private Float standardHourlyRate;
    private Boolean isBilling;
    private String billingTypeString;
}
